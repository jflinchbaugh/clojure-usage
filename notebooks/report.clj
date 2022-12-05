(ns report
  {:nextjournal.clerk/visibility {:code :hide :result :hide}}
  (:require [clojure-usage.core :refer :all]
            [nextjournal.clerk :as clerk]))

^{:nextjournal.clerk/no-cache true}
(defn report [year]
  (->> year
    (str "/home/john/workspace/aoc")
    (top-usages 1000)
    )
  )


^{:nextjournal.clerk/visibility {:code :show :result :show}}
(clerk/table (report "2022"))

^{:nextjournal.clerk/visibility {:code :show :result :show}}
(clerk/table (report "2021"))

^{:nextjournal.clerk/visibility {:code :show :result :show}}
(clerk/table (report "2020"))

^{:nextjournal.clerk/visibility {:code :show :result :show}}
(clerk/table (report "2019"))

^{:nextjournal.clerk/visibility {:code :show :result :show}}
(clerk/table (report "2018"))

