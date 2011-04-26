(ns elmer.core
  (:use [clojure.contrib.duck-streams :only [slurp*]]
        [compojure.core :only [defroutes GET POST ANY]]
        [elmer.config]
        [hiccup.core :only [html]]))

(defn get-time []
  (-> (java.util.Date.) .getTime))

(defn unique []
  (format "%s%s" (get-time) (.substring (str (java.util.UUID/randomUUID)) 0 8)))

(defn make-key []
  (let [r (java.security.SecureRandom.)
        bs (byte-array 6)]
    (.nextBytes r bs)
    (.encode (sun.misc.BASE64Encoder.) bs)))

(defn publish-root []
  (-> (config :publish-root)
      java.io.File.
      .getAbsolutePath))

(defn key-valid? [id key]
  (try
    (= key (slurp* (format "%s/%s.key" (config :key-root) id)))
    (catch java.io.FileNotFoundException _ false)))

(defn store-key [id key]
  (spit (format "%s/%s.key" (config :key-root) id) key))

(defn serve-paste [filename]
  (let [path (format "%s/%s" (publish-root)
                     filename)]
    (if (.exists (java.io.File. path))
      {:status 200
       :headers {"Content-Type" "text/plain"}
       :body (slurp path)}
      {:status 404
       :body (format "%s not found" filename)})))

(defn post-paste [{:keys [uri body] :as request}]
  (let [file (or (-> request :headers (get "x-save-as"))
                 (format "%s.txt" (unique)))
        key (or (-> request :headers (get "x-key")) (make-key))
        key-not-valid (not (key-valid? file key))
        can-edit (not key-not-valid)
        path (format "%s/%s" (publish-root) file)
        paste-url (format "%s/%s" (config :public-url) file)
        body* (slurp* body)
        success (format "%s %s %s\n" (count body*) key paste-url)]
    (if (and (.exists (java.io.File. path)) key-not-valid)
      {:status 401
       :body (format "unauthorized: %s\n" file)}
      (do
        (when key-not-valid
          (store-key file key))
        (spit path body*)
        success))))

(defn info-paste [request]
  (format "curl -s -XPOST -H \"Content-type: text/plain\" --data-binary @- %s"
          (config :public-url)))

(defn home [request]
  (html
   [:p {:style "text-align:center;margin-top:200px"}
    [:img
     {:src "/img/ralph2.gif"}]]))

(defn not-found [request]
  (html
   [:h1 (format "Page not found: %s" (:uri request))]))

(defroutes app
  (GET "/:paste.:ext" [paste ext]
       (serve-paste (format "%s.%s" paste ext)))
  (GET "/sh" request
       (info-paste request))
  (GET "/" request
       (home request))
  (POST "/" request
        (post-paste request))
  (ANY "*" request
       {:status 404, :body (not-found request)}))
