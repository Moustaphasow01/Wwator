# 🌊 Wator – Simulation de dynamique des populations

**Wator** est une simulation écologique mettant en scène deux espèces : **les requins** et **les thons**, cohabitant dans un environnement qui représente un océan virtuel. Le projet modélise les règles de déplacement, de reproduction, de prédation et de survie pour chaque espèce à travers des cycles de simulation.

---

## 🧬 Description du modèle

Deux types d'entités peuplent l’environnement :

- 🐟 **Thons**
- 🦈 **Requins**

Chaque espèce possède ses propres comportements et paramètres.

---

## ⚙️ Paramètres de simulation

### Paramètres généraux

- `nTunas` : nombre initial de **thons**
- `nSharks` : nombre initial de **requins**

### Paramètres individuels

#### Pour chaque **thon** :

- `tBreed` : nombre de cycles avant de pouvoir se reproduire

#### Pour chaque **requin** :

- `sBreed` : nombre de cycles avant de pouvoir se reproduire
- `sEnergy` : énergie initiale du requin  
  - **+1** s'il mange un thon  
  - **–1** à chaque cycle sans repas  
  - Mort si `sEnergy = 0`

---

## 📜 Règles du jeu

### 🐟 Thons

- À chaque cycle :
  - Se déplacent aléatoirement dans une **case libre** du voisinage de **Moore**
  - S'il n’y a pas de case libre : **pas de déplacement**, **pas de reproduction**
  - Après `tBreed` cycles : se **reproduisent** dans leur **ancienne case**

### 🦈 Requins

- À chaque cycle :
  - Cherchent à se déplacer vers une **case occupée par un thon** (prioritaire)
  - Sinon, se déplacent vers une **case libre**
  - S'ils mangent un thon : **+1 énergie**
  - S’ils ne trouvent aucune case libre ou thon : **ils ne bougent pas**, **ne se reproduisent pas**
  - Après `sBreed` cycles : se **reproduisent** dans leur **ancienne case**
  - Si l’énergie atteint **0** : **le requin meurt**

---

## 🛠️ Technologies utilisées

- **Langage** : Scala  
- **Build Tool** : [sbt](https://www.scala-sbt.org/)

---

## 🚀 Lancement du projet

1. Cloner ce dépôt ou se placer dans le dossier contenant le projet.
2. Ouvrir un terminal.
3. Lancer la commande suivante :

```bash
sbt run
