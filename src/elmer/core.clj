(ns elmer.core
  (:require [elmer.store :as store]
            [compojure.route :as route]
            [hiccup.core :as html]
            [elmer.util :refer [unique]]
            [elmer.middleware :refer [wrap-paste-store]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [compojure.core :refer [routes GET POST ANY]]
            )
  (:require [clojure.tools.logging :as log])
  (:use [clojure.string :only [replace-first]]
        [elmer.template :only [render-template]]))

(defn make-key []
  (let [r (java.security.SecureRandom.)
        bs (byte-array 6)]
    (.nextBytes r bs)
    (.encode (sun.misc.BASE64Encoder.) bs)))

(defn serve-paste [store paste]
  (let [bytes (store/get store paste)]
    (if bytes
      {:status 200
       :headers {"Content-Type" "text/plain"}
       :body bytes}
      {:status 404
       :body (format "%s not found" paste)})))

(defn save-as [request]
  (let [uri (replace-first (:uri request) "/" "")
        uri (if (seq uri) uri nil)]
    (or uri
        (-> request :headers (get "x-save-as"))
        (format "%s.txt" (unique)))) )

(defmacro with-saved-body [sym & body]
  `(try
     (let [~sym ]
       ~@body)
     (finally
       (.delete ~sym))))

(defn post-paste [config {:keys [uri body store] :as req}]
  (let [paste (save-as req)
        key (or (-> req :headers (get "x-key"))
                (make-key))
        paste-url (format "%s/%s" (config :public-url) paste)]
    (try
      (if-let [size (store/put store paste key body)]
        (do
          (log/info "store" paste size)
          {:status 200
           :headers {"X-Key" key}
           :body (format "%s %s %s\n" size key paste-url)})
        {:status 401
         :body (format "unauthorized: %s\n" paste)})
      (catch Exception e
        (log/error (type e) (.getMessage e))
        {:status 500
         :body (format "FAIL %s\n" paste)}))))

(defn info-paste [config request]
  (render-template "paste.sh" {:url (config :public-url)}))

(defn home [request]
  (html/html
   [:p {:style "text-align:center;margin-top:200px"}
    [:img
     {:src "/img/ralph2.gif"}]]))

(defn not-found [request]
  (html/html
   [:h1 (format "Page not found: %s" (:uri request))]))

(defn make-routes [config]
  (routes
   (GET "/:paste.:ext" {{paste :paste
                         ext :ext} :params
                         store :store}
        (serve-paste store (format "%s.%s" paste ext)))
   (GET "/sh" request
        (info-paste config request))
   (GET "/" request
        (home request))

   (POST "/:paste.:ext" request
         (post-paste config request))
   (POST "/" request
         (post-paste config request))

   (ANY "*" request
        {:status 404, :body (not-found request)})))

(defn make-handler [config]
  (-> (make-routes routes)
      (wrap-paste-store config)
      (wrap-resource "public")
      wrap-content-type))
