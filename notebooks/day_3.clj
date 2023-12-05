^{:nextjournal.clerk/visibility {:code :hide}}
(ns day-3
  {:nextjournal.clerk/toc true}
  (:require [clojure.set]
            [clojure.string :as str]
            [medley.core :refer [indexed map-vals]]
            [nextjournal.clerk :as clerk]))

;; # [Day 3: Gear Ratios](https://adventofcode.com/2023/day/3)

;; ## Part 1

^{::clerk/visibility {:code :hide
                      :result :hide}}
(def example-input ["467..114.."
                    "...*......"
                    "..35..633."
                    "......#..."
                    "617*......"
                    ".....+.58."
                    "..592....."
                    "......755."
                    "...$.*...."
                    ".664.598.."])

^{::clerk/visibility {:code :hide}}
(clerk/html
 [:details
  [:summary "Toggle puzzle description"]

  [:p "You and the Elf eventually reach a gondola lift station; he says the gondola lift will take you up to the water source, but this is as far as he can bring you. You go inside."]
  [:p "It doesn't take long to find the gondolas, but there seems to be a problem: they're not moving."]
  [:p "Aaah!"]
  [:p "You turn around to see a slightly-greasy Elf with a wrench and a look of surprise. \"Sorry, I wasn't expecting anyone! The gondola lift isn't working right now; it'll still be a while before I can fix it.\" You offer to help."]
  [:p "The engineer explains that an engine part seems to be missing from the engine, but nobody can figure out which one. If you can add up all the part numbers in the engine schematic, it should be easy to work out which part is missing."]
  [:p "The engine schematic (your puzzle input) consists of a visual representation of the engine. There are lots of numbers and symbols you don't really understand, but apparently any number adjacent to a symbol, even diagonally, is a \"part number\" and should be included in your sum. (Periods (.) do not count as a symbol.)"]
  [:p "Here is an example engine schematic:"]
  (into [:pre] (interleave example-input
                           (repeat [:br])))
  [:p "In this schematic, two numbers are not part numbers because they are not adjacent to a symbol: 114 (top right) and 58 (middle right). Every other number is adjacent to a symbol and so is a part number; their sum is 4361."]
  [:p "Of course, the actual engine schematic is much larger. What is the sum of all of the part numbers in the engine schematic?"]])

^{::clerk/visibility {:result :hide}}
(defn parse-row [re s]
  (let [matcher (re-matcher re s)]
    (loop [acc []]
      (if-some [m (re-find matcher)]
        (recur (conj acc {:value (or (parse-long m) m)
                          :start (.start matcher)
                          :end (.end matcher)}))
        acc))))

^{::clerk/visibility {:result :hide}}
(def parse-part-number (partial parse-row #"\d+"))

(parse-part-number "467..114..")

^{::clerk/visibility {:result :hide}}
(def parse-symbol (partial parse-row #"[^\d\.]"))

(parse-symbol "617*......")

^{::clerk/visibility {:result :hide}}
(defn into-map-indexed [coll]
  (into {}
        (partition-all 2)
        (interleave (range) coll)))

^{::clerk/visibility {:result :hide}}
(defn create-symbol-map [coll]
  (->> coll
       (map parse-symbol)
       into-map-indexed
       (map-vals (fn [syms]
                   (into {} (map (juxt :start :value)) syms)))))

(def example-symbol-map (create-symbol-map example-input))

;; ### Example input

;; In this schematic, two numbers are not part numbers because they are not adjacent to a symbol: 114 (top right) and 58 (middle right). Every other number is adjacent to a symbol and so is a part number; their sum is 4361.

^{::clerk/visibility {:result :hide}}
(defn nearby-symbol-in-row [s-map row {:keys [start end]}]
  (when (>= row 0)
    (->> (range (dec start) (inc end)) ; NB: range uses `end` excluding
         (some #(get-in s-map [row %])))))

^{::clerk/visibility {:result :hide}}
(defn get-nearby-symbol [s-map row {:keys [start end] :as v}]
  (or (nearby-symbol-in-row s-map (dec row) v)
      (get-in s-map [row (dec start)])
      (get-in s-map [row end])
      (nearby-symbol-in-row s-map (inc row) v)))

(->> (for [[row s] (indexed example-input)
           part (parse-part-number s)
           :when (get-nearby-symbol example-symbol-map row part)]
       (:value part))
     (reduce + 0))

;; ### Solution

;; Of course, the actual engine schematic is much larger. What is the sum of all of the part numbers in the engine schematic?

^{::clerk/visibility {:result :hide}}
(def puzzle-input (-> (slurp "datasets/day_3.txt")
                      str/split-lines))

^{::clerk/visibility {:result :hide}}
(def symbol-map (create-symbol-map puzzle-input))

(->> (for [[row s] (indexed puzzle-input)
           part (parse-part-number s)
           :when (get-nearby-symbol symbol-map row part)]
       (:value part))
     (reduce + 0))

;; ## Part 2

^{::clerk/visibility {:code :hide}}
(clerk/html
 [:details
  [:summary "Toggle puzzle description"]

  [:p "The engineer finds the missing part and installs it in the engine! As the engine springs to life, you jump in the closest gondola, finally ready to ascend to the water source."]
  [:p "You don't seem to be going very fast, though. Maybe something is still wrong? Fortunately, the gondola has a phone labeled \"help\", so you pick it up and the engineer answers."]
  [:p "Before you can explain the situation, she suggests that you look out the window. There stands the engineer, holding a phone in one hand and waving with the other. You're going so slowly that you haven't even left the station. You exit the gondola."]
  [:p "The missing part wasn't the only issue - one of the gears in the engine is wrong. A gear is any * symbol that is adjacent to exactly two part numbers. Its gear ratio is the result of multiplying those two numbers together."]
  [:p "This time, you need to find the gear ratio of every gear and add them all up so that the engineer can figure out which gear needs to be replaced."]
  [:p "Consider the same engine schematic again:"]
  (into [:pre] (interleave example-input
                           (repeat [:br])))
  [:p "In this schematic, there are two gears. The first is in the top left; it has part numbers 467 and 35, so its gear ratio is 16345. The second gear is in the lower right; its gear ratio is 451490. (The * adjacent to 617 is not a gear because it is only adjacent to one part number.) Adding up all of the gear ratios produces 467835."]
  [:p "What is the sum of all of the gear ratios in your engine schematic?"]])

;; A gear is any * symbol that is adjacent to exactly two part numbers.

^{::clerk/visibility {:result :hide}}
(def parse-gear (partial parse-row #"[\*]"))

^{::clerk/visibility {:result :hide}}
(defn nearby-symbols-in-row [s-map row {:keys [start end]}]
  (let [r1 (set (range start (inc end)))]
    (for [sym (get s-map row)
          :let [r2 (set (range (:start sym) (inc (:end sym))))]
          :when (seq (clojure.set/intersection r1 r2))]
      sym)))

^{::clerk/visibility {:result :hide}}
(defn get-nearby-parts [s-map row v]
  (concat (keep :value (nearby-symbols-in-row s-map (dec row) v))
          (keep :value (nearby-symbols-in-row s-map row v))
          (keep :value (nearby-symbols-in-row s-map (inc row) v))))

;; In this schematic, there are two gears. The first is in the top left; it has part numbers 467 and 35, so its gear ratio is 16345.

^{::clerk/visibility {:code :hide}}
(clerk/html
 (into [:pre] (interleave (take 3 example-input)
                          (repeat [:br]))))

^{::clerk/visibility {:result :hide}}
(def example-part-map (->> example-input
                           (map parse-part-number)
                           into-map-indexed))

(->> (parse-gear "...*......")
     (map (partial get-nearby-parts example-part-map 1)))

;; The second gear is in the lower right; its gear ratio is 451490.

^{::clerk/visibility {:code :hide}}
(clerk/html
 (into [:pre] (interleave (take-last 3 example-input)
                          (repeat [:br]))))

(->> (parse-gear "...$.*....")
     (map (partial get-nearby-parts example-part-map 8)))

;; The * adjacent to 617 is not a gear because it is only adjacent to one part number.

^{::clerk/visibility {:code :hide}}
(clerk/html
 (into [:pre] (interleave (take 3 (drop 3 example-input))
                          (repeat [:br]))))

(->> (parse-gear "617*......")
     (map (partial get-nearby-parts example-part-map 4)))

;; ### Example input

;; Adding up all of the gear ratios produces 467835.

^{::clerk/visibility {:result :hide}}
(def example-gear-parts
  (for [[row s] (indexed example-input)
        gear (parse-gear s)
        :let [parts (get-nearby-parts example-part-map row gear)]
        :when (= 2 (count parts))]
    parts))

(->> example-gear-parts
     (transduce (map (partial apply *)) + 0))

;; ### Solution

^{::clerk/visibility {:result :hide}}
(def part-map (->> puzzle-input
                   (map parse-part-number)
                   into-map-indexed))

^{::clerk/visibility {:result :hide}}
(def gear-parts
  (for [[row s] (indexed puzzle-input)
        gear (parse-gear s)
        :let [parts (get-nearby-parts part-map row gear)]
        :when (= 2 (count parts))]
    parts))

(->> gear-parts
     (transduce (map (partial apply *)) + 0))

