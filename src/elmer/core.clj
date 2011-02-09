(ns elmer.core
  (:use [clojure.contrib.duck-streams :only [slurp*]]
        [elmer.config]
        [hiccup.core :only [html]]))

(defn get-time []
  (-> (java.util.Date.) .getTime))

(defn unique []
  (format "%s%s" (get-time) (.substring (str (java.util.UUID/randomUUID)) 0 8)))

(defn publish-root []
  (-> (config :publish-root)
      java.io.File.
      .getAbsolutePath))

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
        path (format "%s/%s" (publish-root) file)
        paste-url (format "%s/%s" (config :public-url) file)
        body* (slurp* body)]
    (if (.exists (java.io.File. path))
      {:status 409
       :body (format "%s already exists" file)}
      (do
        (spit path body*)
        (format "%s %s" (count body*) paste-url)))))

(defn info-paste [request]
  (format "curl -s -XPOST -H \"Content-type: text/plain\" --data-binary @- %s"
          (config :public-url)))

(defn home [request]
  (html
   [:p {:style "text-align:center;margin-top:200px"}
    [:img
     {:src
      "http://wiw.org/~jess/wp-content/uploads/2007/07/ralph2.gif"}]]))

(defn not-found [request]
  (html
   [:h1 (format "Page not found: %s" (:uri request))]))
