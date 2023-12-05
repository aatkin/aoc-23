(ns index
  {:nextjournal.clerk/visibility {:code :hide}}
  (:require [nextjournal.clerk :as clerk]))

;; # Advent of Code 2023

^{:nextjournal.clerk/visibility {:result :hide}}
(def enabled #{1 2 3})

(clerk/html
 (into [:div.grid.gap-8.sm:grid-cols-4.md:grid-cols-5.py-2]
       (for [n (map inc (range 24))
             :let [path (str "notebooks/day_" n)]]
         (if (contains? enabled n)
           [:a.rounded-lg.shadow-lg.border.border-gray-300.relative.group.w-24.h-24.flex.hover:border-indigo-600
            {:title path
             :href (clerk/doc-url path)}
            [:div.sans-serif.px-4.py-2.group-hover:border-indigo-600
             [:div.font-bold.block.group-hover:text-indigo-600
              n]]]

           [:div.rounded-lg.shadow-lg.border.border-gray-300.relative.group.w-24.h-24.flex
            [:div.sans-serif.px-4.py-2
             [:div.font-bold.block
              n]]]))))
