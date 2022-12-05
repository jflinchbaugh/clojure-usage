(ns clojure-usage.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.core.logic :as l]
            [nextjournal.clerk :as clerk]))

(defn file->symbols [f]
  (->>
   (str "(" (slurp f) ")")
   read-string
   flatten
   (filter (fn [s] (or (symbol? s) (keyword? s))))
   (map (fn [s] (if (symbol? s) (symbol (name s)) s)))))

(def interesting-symbols
  (set
    (concat
      (keys (ns-publics 'clojure.core))
      (keys (ns-publics 'clojure.string))
      (keys (ns-publics 'clojure.test))
      (keys (ns-publics 'clojure.set))
      (keys (ns-publics 'clojure.core.logic))
      '(_ fn* :require :refer :as :all))))

(defn of-interest? [s]
  (interesting-symbols s))

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
   (map (juxt identity (partial top-usages 1000))))

  (->>
   ["aoc2018" "aoc2019" "aoc2020" "aoc2021" "aoc2022"]
   (map (partial str "/home/john/workspace/"))
   (map (juxt identity (partial top-omitted-usages 50))))

;
  )


