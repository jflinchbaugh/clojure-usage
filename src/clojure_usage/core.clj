(ns clojure-usage.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [nextjournal.clerk :as clerk]))

(defn file->symbols [f]
  (->>
   (str "(" (slurp f) ")")
   read-string
   flatten
   (filter (fn [s] (or (symbol? s) (keyword? s))))
   (map (fn [s] (if (symbol? s) (symbol (name s)) s)))))

(defn top-usages [top src-dir]
  (->> src-dir
       io/file
       file-seq
       (filter (fn [f] (and (.isFile f) (re-matches #".*\.clj[cs]*" (str f)))))
       (map file->symbols)
       flatten
       frequencies
       (sort-by second)
       reverse
       (take top)))

(defn start-clerk! []
  (clerk/serve! {:browse? true :watch-paths ["notebooks"]}))

(defn stop-clerk! []
  (clerk/halt!))

(defn build-clerk! [out-path]
  (clerk/build! {:paths ["notebooks/incidents.clj"]
                 :out-path (io/file out-path "clerk")}))
(comment

  (start-clerk!)

  (->>
   ["aoc2018" "aoc2019" "aoc2020" "aoc2021" "aoc2022"]
   (map (partial str "/home/john/workspace/"))
   (map (juxt identity (partial top-usages 40))))
  ;; => (["/home/john/workspace/aoc2018"
  ;;      ([map 53]
  ;;       [defn 45]
  ;;       [->> 33]
  ;;       [is 29]
  ;;       [fn* 25]
  ;;       [= 25]
  ;;       [deftest 17]
  ;;       [first 17]
  ;;       [count 14]
  ;;       [def 12]
  ;;       [fn 11]
  ;;       [filter 10]
  ;;       [let 10]
  ;;       [range 10]
  ;;       [+ 9]
  ;;       [if 8]
  ;;       [second 8]
  ;;       [nil? 7]
  ;;       [ns 7]
  ;;       [concat 7]
  ;;       [rest 7]
  ;;       [split 6])]
  ;;     ["/home/john/workspace/aoc2019"
  ;;      ([= 77]
  ;;       [defn 69]
  ;;       [first 35]
  ;;       [->> 33]
  ;;       [is 33]
  ;;       [testing 32]
  ;;       [let 27]
  ;;       [+ 26]
  ;;       [map 23]
  ;;       [_ 20]
  ;;       [deftest 18]
  ;;       [ns 18]
  ;;       [-> 18]
  ;;       [count 17]
  ;;       [are 17]
  ;;       [:refer 16]
  ;;       [:require 16]
  ;;       [* 16]
  ;;       [def 15]
  ;;       [if 15]
  ;;       [second 15]
  ;;       [recur 13]
  ;;       [nil? 13])]
  ;;     ["/home/john/workspace/aoc2020"
  ;;      ([defn 153]
  ;;       [map 93]
  ;;       [->> 86]
  ;;       [count 59]
  ;;       [first 54]
  ;;       [let 44]
  ;;       [fn* 43]
  ;;       [def 39]
  ;;       [filter 36]
  ;;       [+ 34]
  ;;       [reduce 33]
  ;;       [range 30]
  ;;       [ns 28]
  ;;       [= 28]
  ;;       [comment 25]
  ;;       [:as 24]
  ;;       [- 22]
  ;;       [fn 22]
  ;;       [:require 22]
  ;;       [-> 22]
  ;;       [* 22]
  ;;       [dec 22]
  ;;       [clojure.string 21]
  ;;       [partial 21]
  ;;       [l 20]
  ;;       [set 19]
  ;;       [trim 18]
  ;;       [inc 18]
  ;;       [for 18])]
  ;;     ["/home/john/workspace/aoc2021"
  ;;      ([= 113]
  ;;       [defn 106]
  ;;       [map 85]
  ;;       [->> 77]
  ;;       [is 76]
  ;;       [deftest 58]
  ;;       [first 57]
  ;;       [:refer 53]
  ;;       [:all 53]
  ;;       [fn* 50]
  ;;       [let 37]
  ;;       [ns 36]
  ;;       [:require 36]
  ;;       [count 35]
  ;;       [filter 34]
  ;;       [fn 32]
  ;;       [second 26]
  ;;       [str 24]
  ;;       [inc 23]
  ;;       [range 23]
  ;;       [testing 20]
  ;;       [reduce 20]
  ;;       [def 19])]
  ;;     ["/home/john/workspace/aoc2022"
  ;;      ([defn 26]
  ;;       [map 24]
  ;;       [is 18]
  ;;       [deftest 14]
  ;;       [:as 14]
  ;;       [->> 13]
  ;;       [= 13]
  ;;       [+ 10]
  ;;       [ns 10]
  ;;       [fn 10]
  ;;       [:require 10]
  ;;       [reduce 10]
  ;;       [def 6]
  ;;       [:refer 6]
  ;;       [:all 6]
  ;;       [set 6]
  ;;       [count 5]
  ;;       [comment 5]
  ;;       [inc 5]
  ;;       [/ 5]
  ;;       [clojure.test 5]
  ;;       [range 5])]

  ;
  )


