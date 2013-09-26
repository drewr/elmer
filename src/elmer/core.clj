(ns elmer.core
  (:require [elmer.store :as store]
            [compojure.route :as route]
            [hiccup.core :as html]
            [elmer.util :refer [unique]]
            [elmer.middleware :refer [wrap-paste-store]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [compojure.core :as http]
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

(defn post-paste [{:keys [uri body store elmer] :as req}]
  (let [paste (save-as req)
        key (or (-> req :headers (get "x-key"))
                (make-key))
        paste-url (format "%s/%s" (-> elmer :public-url) paste)]
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

(defn info-paste [request]
  (render-template (-> request :elmer :template-root)
                   "paste.sh" {:url (-> request :elmer :public-url)}))

(defn home [request]
  (html/html
   [:p {:style "text-align:center;margin-top:200px"}
    [:img
     {:src "/img/ralph2.gif"}]]))

(defn not-found [request]
  (html/html
   [:h1 (format "Page not found: %s" (:uri request))]))

(def routes
  (http/routes
   (http/GET "/:paste.:ext"
             {{paste :paste ext :ext} :params store :store}
             (serve-paste store (format "%s.%s" paste ext)))
   (http/GET "/sh" request
             (info-paste request))
   (http/GET "/" request
             (home request))
   (http/POST "/:paste.:ext" request
              (post-paste request))
   (http/POST "/" request
              (post-paste request))

   (http/ANY "*" request
             {:status 404, :body (not-found request)})))

(defn wrap-config [app conf]
  (fn [req]
    (app (assoc req :elmer conf))))

(defn make-handler [config]
  (-> routes
      wrap-paste-store
      (wrap-resource "public")
      wrap-content-type
      (wrap-config config)))
