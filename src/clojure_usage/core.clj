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
   (map (juxt identity (partial top-usages 20))))
  ;; => (["/home/john/workspace/aoc2018"
  ;;      ([map 53]
  ;;       [defn 45]
  ;;       [v 35]
  ;;       [->> 33]
  ;;       [is 29]
  ;;       [input 26]
  ;;       [fn* 25]
  ;;       [= 25]
  ;;       [deftest 17]
  ;;       [first 17]
  ;;       [count 14]
  ;;       [m 14]
  ;;       [def 12]
  ;;       [record 12]
  ;;       [s 12]
  ;;       [x 12]
  ;;       [fn 11]
  ;;       [filter 10]
  ;;       [let 10]
  ;;       [claim-string 10])]
  ;;     ["/home/john/workspace/aoc2019"
  ;;      ([= 77]
  ;;       [defn 69]
  ;;       [:outputs 57]
  ;;       [first 35]
  ;;       [->> 33]
  ;;       [is 33]
  ;;       [outputs 32]
  ;;       [testing 32]
  ;;       [state 30]
  ;;       [:with 30]
  ;;       [inputs 28]
  ;;       [let 27]
  ;;       [+ 26]
  ;;       [run-int-code 26]
  ;;       [part-1 26]
  ;;       [input 24]
  ;;       [map 23]
  ;;       [base 22]
  ;;       [part-2 21]
  ;;       [_ 20])]
  ;;     ["/home/john/workspace/aoc2020"
  ;;      ([defn 153]
  ;;       [input 115]
  ;;       [map 93]
  ;;       [->> 86]
  ;;       [count 59]
  ;;       [first 54]
  ;;       [v 52]
  ;;       [part-1 50]
  ;;       [let 44]
  ;;       [fn* 43]
  ;;       [x 43]
  ;;       [y 41]
  ;;       [def 39]
  ;;       [part-2 38]
  ;;       [filter 36]
  ;;       [+ 34]
  ;;       [reduce 33]
  ;;       [range 30]
  ;;       [parse 29]
  ;;       [ns 28])]
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
  ;;       [part-1 47]
  ;;       [let 37]
  ;;       [ns 36]
  ;;       [:require 36]
  ;;       [count 35]
  ;;       [filter 34]
  ;;       [registers 34]
  ;;       [coords 33]
  ;;       [part-2 33]
  ;;       [fn 32])]
  ;;     ["/home/john/workspace/aoc2022"
  ;;      ([defn 26]
  ;;       [map 24]
  ;;       [is 18]
  ;;       [deftest 14]
  ;;       [:as 14]
  ;;       [->> 13]
  ;;       [= 13]
  ;;       [part-1 12]
  ;;       [part-2 12]
  ;;       [s 11]
  ;;       [file->lines 10]
  ;;       [+ 10]
  ;;       [ns 10]
  ;;       [fn 10]
  ;;       [:require 10]
  ;;       [reduce 10]
  ;;       [round-scores 7]
  ;;       [def 6]
  ;;       [:refer 6]
  ;;       [aoc2022.core 6])])

  ;
  )


