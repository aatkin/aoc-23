(ns user
  (:require [clojure.tools.namespace.repl :as repl]
            [nextjournal.clerk :as clerk]))

(comment
  (repl/clear)
  (repl/refresh-all)

  (clerk/serve! {:browse? true
                 :watch-paths ["notebooks" "index.clj"]})
  (clerk/clear-cache!))
