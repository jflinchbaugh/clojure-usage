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

(defn of-interest? [s]
  ((set
    '(* + - -> ->> / = _
        :all are :as apply
        blank?
        clojure.test comment concat count comp complement constantly
        dec def defn deftest
        filter first fn fn* for
        get-in get group-by
        if inc is iterate
        let
        map max mapcat
        nil? ns nth
        partial partition
        range recur reduce :refer :require rest remove re-matches
        second set split str
        testing trim))
   s))

(defn top-usages [top src-dir]
  (->> src-dir
       io/file
       file-seq
       (filter (fn [f] (and (.isFile f) (re-matches #".*\.clj[cs]*" (str f)))))
       (map file->symbols)
       flatten
       (filter of-interest?)
       frequencies
       (sort-by second)
       reverse
       (take top)))

(defn top-omitted-usages [top src-dir]
  (->> src-dir
       io/file
       file-seq
       (filter (fn [f] (and (.isFile f) (re-matches #".*\.clj[cs]*" (str f)))))
       (map file->symbols)
       flatten
       (filter (complement of-interest?))
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
  ;;       [split 6]
  ;;       [remove 6]
  ;;       [reduce 6]
  ;;       [str 5]
  ;;       [trim 4]
  ;;       [apply 4]
  ;;       [inc 4]
  ;;       [for 4]
  ;;       [partition 4]
  ;;       [:require 4]
  ;;       [:as 4]
  ;;       [complement 3]
  ;;       [comment 3]
  ;;       [recur 3]
  ;;       [dec 3]
  ;;       [:refer 2]
  ;;       [max 2]
  ;;       [partial 2]
  ;;       [- 2])]
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
  ;;       [nil? 13]
  ;;       [range 13]
  ;;       [str 12]
  ;;       [partial 12]
  ;;       [:all 12]
  ;;       [:as 12]
  ;;       [comp 12]
  ;;       [comment 11]
  ;;       [fn* 11]
  ;;       [apply 10]
  ;;       [set 9]
  ;;       [for 8]
  ;;       [filter 7]
  ;;       [- 7]
  ;;       [remove 7]
  ;;       [get 6]
  ;;       [rest 6]
  ;;       [trim 5])]
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
  ;;       [partial 21]
  ;;       [set 19]
  ;;       [trim 18]
  ;;       [inc 18]
  ;;       [for 18]
  ;;       [second 18]
  ;;       [str 17]
  ;;       [concat 16]
  ;;       [if 16]
  ;;       [apply 14]
  ;;       [nth 14]
  ;;       [:refer 13]
  ;;       [:all 13]
  ;;       [remove 13]
  ;;       [comp 13]
  ;;       [iterate 13]
  ;;       [split 12]
  ;;       [get-in 10])]
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
  ;;       [def 19]
  ;;       [are 19]
  ;;       [+ 18]
  ;;       [split 18]
  ;;       [clojure.test 18]
  ;;       [comment 17]
  ;;       [comp 17]
  ;;       [if 16]
  ;;       [:as 14]
  ;;       [for 13]
  ;;       [set 13]
  ;;       [mapcat 12]
  ;;       [* 11]
  ;;       [rest 11]
  ;;       [constantly 10]
  ;;       [nth 10]
  ;;       [dec 10]
  ;;       [- 9])]
  ;;     ["/home/john/workspace/aoc2022"
  ;;      ([defn 33]
  ;;       [map 31]
  ;;       [is 23]
  ;;       [->> 19]
  ;;       [= 18]
  ;;       [deftest 17]
  ;;       [:as 17]
  ;;       [fn 13]
  ;;       [ns 12]
  ;;       [:require 12]
  ;;       [reduce 12]
  ;;       [+ 10]
  ;;       [def 8]
  ;;       [:refer 7]
  ;;       [:all 7]
  ;;       [count 6]
  ;;       [comment 6]
  ;;       [inc 6]
  ;;       [nth 6]
  ;;       [clojure.test 6]
  ;;       [range 6]
  ;;       [dec 6]
  ;;       [set 6]
  ;;       [apply 5]
  ;;       [str 5]
  ;;       [/ 5]
  ;;       [let 5]
  ;;       [blank? 3]
  ;;       [filter 3]
  ;;       [split 3]
  ;;       [partition 3]
  ;;       [first 3]
  ;;       [complement 2]
  ;;       [concat 2]
  ;;       [remove 2]
  ;;       [testing 2]
  ;;       [rest 2]
  ;;       [are 2]
  ;;       [partial 1]
  ;;       [nil? 1])])

  (->>
   ["aoc2018" "aoc2019" "aoc2020" "aoc2021" "aoc2022"]
   (map (partial str "/home/john/workspace/"))
   (map (juxt identity (partial top-omitted-usages 40))))

;
  )


