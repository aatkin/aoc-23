^{:nextjournal.clerk/visibility {:code :hide}}
(ns day-1
  {:nextjournal.clerk/toc true}
  (:require [clojure.string :as str]
            [nextjournal.clerk :as clerk]))

;; # Day 1

;; ## Part 1

;; On each line, the calibration value can be found by combining the first digit and the last digit (in that order) to form a single two-digit number.

^{::clerk/visibility {:result :hide}}
(defn find-digits [s]
  (map #(first (re-seq #"\d" %)) [s (str/reverse s)]))

^{::clerk/visibility {:result :hide}}
(defn parse-int [x]
  (Integer/parseInt x))

;; ### Example input

^{::clerk/visibility {:result :hide}}
(def example-input
  (str/join "\n" ["1abc2"
                  "pqr3stu8vwx"
                  "a1b2c3d4e5f"
                  "treb7uchet"]))

;; In this example, the calibration values of these four lines are 12, 38, 15, and 77. Adding these together produces 142.

(def digits-by-line (->> (str/split-lines example-input)
                         (map find-digits)))

(->> digits-by-line
     (map str/join)
     (map parse-int)
     (reduce + 0))

;; ### Solution

^{::clerk/visibility {:result :hide}}
(def puzzle-input (-> (slurp "datasets/day_1.txt")
                      str/split-lines))

(->> puzzle-input
     (transduce (map (comp parse-int str/join find-digits))
                +
                0))

;; ## Part 2

;; ### Extended digits

^{::clerk/visibility {:result :hide}}
(def numbers ["one" "two" "three" "four" "five" "six" "seven" "eight" "nine"])

^{::clerk/visibility {:result :hide}}
(def +digit+ (re-pattern (->> (cons "\\d" numbers)
                              (str/join "|"))))

(re-seq +digit+ "two1nine")

^{::clerk/visibility {:result :hide}}
(def +reverse-digit+ (re-pattern (->> (map str/reverse numbers)
                                      (cons "\\d")
                                      (str/join "|"))))

(->> (str/reverse "two1nine")
     (re-seq +reverse-digit+)
     (map str/reverse))

^{::clerk/visibility {:result :hide}}
(def extended-digits
  (->> (map (comp str inc) (range 9))
       (interleave numbers)
       (into {} (partition-all 2))))

^{::clerk/visibility {:result :hide}}
(defn parse-extended-digit [s]
  (get extended-digits s s))

^{::clerk/visibility {:result :hide}}
(defn find-extended-digits [s]
  (list (-> (re-seq +digit+ s)
            first
            parse-extended-digit)
        (-> (re-seq +reverse-digit+ (str/reverse s))
            first
            str/reverse
            parse-extended-digit)))

;; ### Example input

^{::clerk/visibility {:result :hide}}
(def example-input-2
  (str/join "\n" ["two1nine"
                  "eightwothree"
                  "abcone2threexyz"
                  "xtwone3four"
                  "4nineeightseven2"
                  "zoneight234"
                  "7pqrstsixteen"]))

;; In this example, the calibration values are 29, 83, 13, 24, 42, 14, and 76. Adding these together produces 281.

(def extended-digits-by-line (->> (str/split-lines example-input-2)
                                  (map find-extended-digits)))

(->> extended-digits-by-line
     (map str/join)
     (map parse-int)
     (reduce + 0))

;; What is the sum of all of the calibration values?

;; ### Solution

(->> puzzle-input
     (transduce (map (comp parse-int str/join find-extended-digits))
                +
                0))

;; ## Appendix

;; ### Day 1 description

;; Something is wrong with global snow production, and you've been selected to take a look. The Elves have even given you a map; on it, they've used stars to mark the top fifty locations that are likely to be having problems.

;; You've been doing this long enough to know that to restore snow operations, you need to check all fifty stars by December 25th.

;; Collect stars by solving puzzles. Two puzzles will be made available on each day in the Advent calendar; the second puzzle is unlocked when you complete the first. Each puzzle grants one star. Good luck!

;; You try to ask why they can't just use a weather machine ("not powerful enough") and where they're even sending you ("the sky") and why your map looks mostly blank ("you sure ask a lot of questions") and hang on did you just say the sky ("of course, where do you think snow comes from") when you realize that the Elves are already loading you into a trebuchet ("please hold still, we need to strap you in").

;; As they're making the final adjustments, they discover that their calibration document (your puzzle input) has been amended by a very young Elf who was apparently just excited to show off her art skills. Consequently, the Elves are having trouble reading the values on the document.

;; The newly-improved calibration document consists of lines of text; each line originally contained a specific calibration value that the Elves now need to recover. On each line, the calibration value can be found by combining the first digit and the last digit (in that order) to form a single two-digit number.

;; For example:

;; 1abc2
;; pqr3stu8vwx
;; a1b2c3d4e5f
;; treb7uchet

;; In this example, the calibration values of these four lines are 12, 38, 15, and 77. Adding these together produces 142.

;; Consider your entire calibration document. What is the sum of all of the calibration values?

;; ### Day 2 description

;; Your calculation isn't quite right. It looks like some of the digits are actually spelled out with letters: one, two, three, four, five, six, seven, eight, and nine also count as valid "digits".

;; Equipped with this new information, you now need to find the real first and last digit on each line. For example:

;; two1nine
;; eightwothree
;; abcone2threexyz
;; xtwone3four
;; 4nineeightseven2
;; zoneight234
;; 7pqrstsixteen

;; In this example, the calibration values are 29, 83, 13, 24, 42, 14, and 76. Adding these together produces 281.

;; What is the sum of all of the calibration values?
