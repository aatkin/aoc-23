^{:nextjournal.clerk/visibility {:code :hide}}
(ns day-2
  {:nextjournal.clerk/toc true}
  (:require [clojure.string :as str]
            [nextjournal.clerk :as clerk]))

;; # Day 2: Cube Conundrum

;; ## Part 1

^{::clerk/visibility {:code :hide
                      :result :hide}}
(def example-input ["Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green"
                    "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue"
                    "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red"
                    "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red"
                    "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green"])

^{::clerk/visibility {:code :hide}}
(clerk/html
 [:details
  [:summary "Toggle puzzle description"]

  [:p "You're launched high into the atmosphere! The apex of your trajectory just barely reaches the surface of a large island floating in the sky. You gently land in a fluffy pile of leaves. It's quite cold, but you don't see much snow. An Elf runs over to greet you."]
  [:p "The Elf explains that you've arrived at Snow Island and apologizes for the lack of snow. He'll be happy to explain the situation, but it's a bit of a walk, so you have some time. They don't get many visitors up here; would you like to play a game in the meantime?"]
  [:p "As you walk, the Elf shows you a small bag and some cubes which are either red, green, or blue. Each time you play this game, he will hide a secret number of cubes of each color in the bag, and your goal is to figure out information about the number of cubes."]
  [:p "To get information, once a bag has been loaded with cubes, the Elf will reach into the bag, grab a handful of random cubes, show them to you, and then put them back in the bag. He'll do this a few times per game."]
  [:p "You play several games and record the information from each game (your puzzle input). Each game is listed with its ID number (like the 11 in Game 11: ...) followed by a semicolon-separated list of subsets of cubes that were revealed from the bag (like 3 red, 5 green, 4 blue)."]
  [:p "For example, the record of a few games might look like this:"]
  (into [:p] (interleave example-input (repeat [:br])))
  [:p "In game 1, three sets of cubes are revealed from the bag (and then put back again). The first set is 3 blue cubes and 4 red cubes; the second set is 1 red cube, 2 green cubes, and 6 blue cubes; the third set is only 2 green cubes."]
  [:p "The Elf would first like to know which games would have been possible if the bag contained only 12 red cubes, 13 green cubes, and 14 blue cubes?"]
  [:p "In the example above, games 1, 2, and 5 would have been possible if the bag had been loaded with that configuration. However, game 3 would have been impossible because at one point the Elf showed you 20 red cubes at once; similarly, game 4 would also have been impossible because the Elf showed you 15 blue cubes at once. If you add up the IDs of the games that would have been possible, you get 8."]
  [:p "Determine which games would have been possible if the bag had been loaded with only 12 red cubes, 13 green cubes, and 14 blue cubes. What is the sum of the IDs of those games?"]])

example-input

^{::clerk/visibility {:code :hide}}
(clerk/table {:head ["Game" "Set 1" "Set 2" "Set 3"]
              :rows (for [row example-input
                          :let [game-id (subs row 5 6)
                                sets (str/split (subs row 8) #"; ")]]
                      (apply list game-id sets))})

;; ### Legal game?

;; In game 1, three sets of cubes are revealed from the bag (and then put back again). The first set is 3 blue cubes and 4 red cubes; the second set is 1 red cube, 2 green cubes, and 6 blue cubes; the third set is only 2 green cubes.

(first example-input)

^{::clerk/visibility {:result :hide}}
(def +game-id+ #"(?<=Game )\d+")

^{::clerk/visibility {:result :hide}}
(def +red+ #"\d+(?= red)")

^{::clerk/visibility {:result :hide}}
(def +green+ #"\d+(?= green)")

^{::clerk/visibility {:result :hide}}
(def +blue+ #"\d+(?= blue)")

{:game-id (re-find +game-id+ (first example-input))
 :red (re-seq +red+ (first example-input))
 :green (re-seq +green+ (first example-input))
 :blue (re-seq +blue+ (first example-input))}

;; Determine which games would have been possible if the bag had been loaded with only 12 red cubes, 13 green cubes, and 14 blue cubes.

^{::clerk/visibility {:result :hide}}
(def game-config {:red 12 :green 13 :blue 14})

;; In the example above, games 1, 2, and 5 would have been possible if the bag had been loaded with that configuration. However, game 3 would have been impossible because at one point the Elf showed you 20 red cubes at once; similarly, game 4 would also have been impossible because the Elf showed you 15 blue cubes at once.

^{::clerk/visibility {:result :hide}}
(defn legal-game? [{:keys [red green blue]} result]
  (and (every? (partial >= red) (:red result))
       (every? (partial >= green) (:green result))
       (every? (partial >= blue) (:blue result))))

(map (partial legal-game? game-config)
     [{:red [20] :green [0] :blue [0]}
      {:red [0] :green [0] :blue [15]}
      {:red [12 12] :green [13 13] :blue [14 14]}])

;; ### Parse sums

;; If you add up the IDs of the games that would have been possible, you get 8.

^{::clerk/visibility {:result :hide}}
(defn parse-results [s]
  {:game-id (parse-long (re-find +game-id+ s))
   :red (map parse-long (re-seq +red+ s))
   :green (map parse-long (re-seq +green+ s))
   :blue (map parse-long (re-seq +blue+ s))})

(->> example-input
     (transduce (comp (map parse-results)
                      (filter (partial legal-game? game-config))
                      (map :game-id))
                + 0))

;; ### Solution

;; What is the sum of the IDs of those games?

(->> (slurp "datasets/day_2.txt")
     str/split-lines
     (transduce (comp (map parse-results)
                      (filter (partial legal-game? game-config))
                      (map :game-id))
                + 0))

;; ## Part 2

^{::clerk/visibility {:code :hide}}
(clerk/html
 [:details
  [:summary "Toggle puzzle description"]

  [:p "The Elf says they've stopped producing snow because they aren't getting any water! He isn't sure why the water stopped; however, he can show you how to get to the water source to check it out for yourself. It's just up ahead!"]
  [:p "As you continue your walk, the Elf poses a second question: in each game you played, what is the fewest number of cubes of each color that could have been in the bag to make the game possible?"]
  [:p "Again consider the example games from earlier:"]
  (into [:p] (interleave example-input (repeat [:br])))
  [:p
   [:ul
    [:li "In game 1, the game could have been played with as few as 4 red, 2 green, and 6 blue cubes. If any color had even one fewer cube, the game would have been impossible."]
    [:li "Game 2 could have been played with a minimum of 1 red, 3 green, and 4 blue cubes."]
    [:li "Game 3 must have been played with at least 20 red, 13 green, and 6 blue cubes."]
    [:li "Game 4 required at least 14 red, 3 green, and 15 blue cubes."]
    [:li "Game 5 needed no fewer than 6 red, 3 green, and 2 blue cubes in the bag."]]]
  [:p "The power of a set of cubes is equal to the numbers of red, green, and blue cubes multiplied together. The power of the minimum set of cubes in game 1 is 48. In games 2-5 it was 12, 1560, 630, and 36, respectively. Adding up these five powers produces the sum 2286."]
  [:p "For each game, find the minimum set of cubes that must have been present. What is the sum of the power of these sets?"]])

;; ### Fewest amount of cubes

;; As you continue your walk, the Elf poses a second question: in each game you played, what is the fewest number of cubes of each color that could have been in the bag to make the game possible?

^{::clerk/visibility {:result :hide}}
(defn parse-max-cubes-sum [results]
  (* (apply max (:red results))
     (apply max (:green results))
     (apply max (:blue results))))

(->> example-input
     (transduce (comp (map parse-results)
                      (map parse-max-cubes-sum))
                + 0))

;; ## Solution

(->> (slurp "datasets/day_2.txt")
     str/split-lines
     (transduce (comp (map parse-results)
                      (map parse-max-cubes-sum))
                + 0))
