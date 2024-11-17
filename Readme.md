# Rapport TP/TD Programmation parallèle / avancée

## TP 1 : Simulation du mouvement d'un ou plusieurs mobiles

### Introduction

En Java, un thread est une unité d'exécution permettant d'effectuer plusieurs tâches simultanément dans un programme. L'interface `Runnable` offre une manière d'implémenter des threads sans hériter de la classe `Thread`. En implémentant `Runnable`, on définit la méthode `run()` qui contient le code que le thread doit exécuter. Ensuite, un objet `Thread` utilise cette instance de `Runnable` pour lancer l'exécution concurrente avec la méthode `start()`. L'utilisation de `Runnable` est souvent privilégiée, car elle permet de séparer la logique métier de l'implémentation du thread, offrant ainsi plus de flexibilité.

### Exercice 1

Dans l'exercice 1, il est demandé de créer une simple fenêtre avec un petit carré (UnMobile) se déplaçant de gauche à droite.  
Le code du mobile étant déjà implémenté, la seule chose à faire est de créer le code de la fenêtre avec les lignes suivantes :

```java
class UneFenetre extends JFrame // Création de la classe
{
    UnMobile sonMobile; // déclaration du mobile
    private final int LARG=400, HAUT=250; // déclaration des constantes largeur + hauteur
    
    public UneFenetre()
    {
        sonMobile = new UnMobile(LARG, HAUT); // Ici on instancie la classe un Mobile 
        add(sonMobile); // Cette ligne ajoute la classe sonMobile au JFrame

        Thread threadMobile = new Thread(sonMobile); // Cette ligne crée le thread sonMobile ; pour l'instant il est juste créé et reste éteint
        threadMobile.start(); // Cette fonction démarre le thread et exécute la fonction run de notre classe sonMobile.

        setSize(LARG, HAUT); // Définition des dimensions de la fenêtre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Définition de l'opération par défaut lors de la fermeture de la fenêtre Java
        setVisible(true); // Affiche la fenêtre
    }
}
```

Ici, il s'agit simplement de créer un thread et de le lancer avec `.start()`.

Par la suite, il faudra faire en sorte que le mobile reparte en sens inverse lorsqu'il atteint une extrémité de la fenêtre. Pour cela, plusieurs choix étaient possibles ; j'ai personnellement choisi celui-ci :

```java
public void run()
{
    for (sonDebDessin=0; sonDebDessin < saLargeur - sonPas; sonDebDessin+= sonPas)
    {
        repaint();
        try{Thread.sleep(sonTemps);}
        catch (InterruptedException telleExcp)
            {telleExcp.printStackTrace();}
    }
    for (sonDebDessin=saLargeur - sonPas; sonDebDessin >= 0 + sonPas; sonDebDessin-= sonPas)
    {
        repaint();                                      // Duplication de la boucle for avec un compteur partant de la valeur de fin de l'autre
        try{Thread.sleep(sonTemps);}                    // Et arrivant jusqu'à 0 (+ sonPas pour assurer un petit décalage esthétique)
        catch (InterruptedException telleExcp)          // sonMobile partira de l'extrême droite pour finir à l'extrême gauche
            {telleExcp.printStackTrace();}
    }
}
```

### Exercice 2

Dans l'exercice 2, on ajoute un simple bouton start/stop pour gérer l'exécution du threadMobile (avec les méthodes `.suspend()` & `.resume()` de la classe Thread).

Pour cela, on ajoute le code suivant à la classe `UneFenetre` :

```java
class UneFenetre extends JFrame // Création de la classe
{
    UnMobile sonMobile; // déclaration du mobile
    private final int LARG=400, HAUT=250; // déclaration des constantes largeur + hauteur
    boolean isRunning = true; // Variable de gestion de l'état du thread
    
    public UneFenetre()
    {
        sonMobile = new UnMobile(LARG, HAUT); // Ici on instancie la classe un Mobile 
        add(sonMobile); // Cette ligne ajoute la classe sonMobile au JFrame

        JButton controlButton = new JButton("Start/Stop"); // Ici on crée une classe JButton avec comme titre "Start/Stop"
        add(controlButton, "South");  // Ajout du bouton au JFrame en position "sud" (autrement dit en bas de la fenêtre)

        Thread threadMobile = new Thread(sonMobile); // Cette ligne crée le thread sonMobile, pour le moment il est juste créé et reste éteint
        threadMobile.start(); // Cette fonction démarre le thread et exécute la fonction run de notre classe sonMobile.

        /*
            Le bloc d'instructions ci-dessous ajoute un "event handler" pour déclencher la fonction donnée en paramètre en fonction d'un évènement particulier (ici, appuyer sur le bouton Start/Stop).

            *Notez que la variable isRunning permet le bon suivi de l'exécution du thread*
        */
        controlButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isRunning) {
                    threadMobile.suspend(); // Grâce à cette fonction, le thread du mobile s'arrête et attend la fonction .resume()
                } else {
                    threadMobile.resume(); // Ici, le thread mis en pause avec .suspend() reprend là où il s'était arrêté
                }
                isRunning = !isRunning;
            }
        });

        setSize(LARG, HAUT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
```

### Exercice 3

L'exercice 3 nous demande de modifier le code en utilisant la classe GridLayout pour créer une grille de 2 par 2 avec un bouton et un mobile pour chaque cellule. J'ai décidé de ne pas reprendre exactement le code de l'exercice pour mieux intégrer cet exercice dans mon programme. Voici le résultat :

```java
class UneFenetre extends JFrame 
{
    UnMobile sonMobile1, sonMobile2;
    JButton controlButton1, controlButton2;
    Thread threadMobile1, threadMobile2;
    private final int LARG=400, HAUT=250;
    boolean isRunning1 = true, isRunning2 = true;
    
    public UneFenetre()
    {
        // Paramétrage de la grille de 2 par 2
        Container leConteneur = getContentPane();
        leConteneur.setLayout(new GridLayout(2, 2));

        // Création du premier mobile / bouton / thread
        sonMobile1 = new UnMobile(LARG, HAUT);
        controlButton1 = new JButton("Start/Stop");

        leConteneur.add(controlButton1, "South");
        leConteneur.add(sonMobile1);

        threadMobile1 = new Thread(sonMobile1);
        threadMobile1.start();

        controlButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isRunning1) {
                    threadMobile1.suspend();
                } else {
                    threadMobile1.resume();
                }
                isRunning1 = !isRunning1;
            }
        });

        // Création du deuxième mobile / bouton / thread
        sonMobile2 = new UnMobile(LARG, HAUT);
        controlButton2 = new JButton("Start/Stop");

        leConteneur.add(controlButton2, "South");
        leConteneur.add(sonMobile2);

        threadMobile2 = new Thread(sonMobile2);
        threadMobile2.start();

        controlButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isRunning2) {
                    threadMobile2.suspend();
                } else {
                    threadMobile2.resume();
                }
                isRunning2 = !isRunning2;
            }
        });

        // Paramétrage de la fenêtre
        setSize(LARG*2, HAUT*2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
```

Ici, j'ai simplement dupliqué le code servant à créer un bouton et un thread mobile pour en gérer un deuxième. Le fonctionnement est exactement le même, sauf que cette fois, la classe Container aura un layout de type GridLayout, formant une grille 2x2. La gestion des threads reste inchangée.

Voici le schéma UML final du TP1 :

![uml](res/tp1ulmschema.png)

## TP 2 : Affichage - Exclusion et Semaphore

Dans le TP2, on introduit le concept de `Semaphore`.  
Un `Semaphore` est un moyen de contrôler l'accès à une ressource partagée entre différents threads pour éviter les conflits et optimiser l'utilisation d'un programme.

Tout le fonctionnement du sémaphore repose sur sa valeur entière. Voici comment elle est manipulée dans la classe :

- Une fonction `syncWait()` vérifie la valeur du sémaphore ; si elle est égale à 0, la fonction bloque l'exécution du thread avec la méthode `wait()`, sinon elle décrémente cette valeur de un.
- Une fonction `syncSignal()` ré-incrémente la valeur interne du sémaphore de un et débloque tous les threads précédemment bloqués sur `wait()` grâce à la fonction `notifyAll()`.

La problématique du TP2 est la suivante :

Comment s'assurer que la console affiche bien un message au format `AABBB` ?

Comme la gestion de l'affichage est multi-thread, les threads ne gèrent pas la priorité d'affichage, ce qui entraîne un affichage désordonné dans la console, par exemple `ABABB`. Les threads peuvent s'exécuter en même temps, produisant un mauvais résultat. Pour éviter cela, on utilise la classe `Semaphore` pour protéger la boucle `for` qui gère l'affichage en tant que ressource critique. Voici le fonctionnement :

- 4 threads sont lancés en même temps : "AAA", "BB", "CCCC", "DDD".
- Le premier thread qui atteint la fonction `syncWait()` décrémente la valeur du `Semaphore` à 0 et, ce faisant, verrouille tous les autres threads qui atteindront la fonction `wait()`, car le `Semaphore` est à 0.
- Lorsque le thread non verrouillé termine son exécution, il atteint la fonction `syncSignal()`, qui ré-incrémente la valeur du sémaphore et déverrouille tous les autres threads.
- Le premier thread qui atteindra à nouveau `syncWait()` reproduira le même comportement, et la boucle continuera jusqu'à la fin du programme.

Grâce à ce mécanisme, les threads s’empêchent mutuellement d’exécuter la même partie de code "critique" en même temps.

Voici le schéma UML final du TP2 :

![uml](res/tp2ulmschema.png)

## TP 3 : Boîte aux lettres et boulangerie  

Le TP 3 introduit le concept de **Blocking Queue** avec, dans un premier temps, l'implémentation simple d'une boîte aux lettres.  
Un producteur dépose une lettre, et un consommateur en retire une.  
Cependant, il y a plusieurs conditions :  

- Le producteur ne dépose sa "lettre" dans la boîte aux lettres (BAL) que si elle est vide.  
- Le consommateur ne retire une "lettre" de la BAL que si elle en contient une.  

On peut déjà voir, à travers cet énoncé, que le producteur et le consommateur sont deux threads distincts, et que la boîte aux lettres constitue ici une section critique.  

La boîte aux lettres fera office de file d'attente bloquante pour les threads, afin de respecter les différentes conditions.  

```java
public synchronized void send(String letter) {
        try {
            while(currentLetter != null) {
                wait();   // Ici, si la boîte aux lettres contient déjà une lettre, le producteur sera obligé de s'arrêter à son prochain envoi.
            }

            System.out.println("LetterBox send: " + letter);
            currentLetter = letter;
            notify();     // Ici, on débloque le thread du consommateur s'il est bloqué à l'instruction wait().
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized String receive() {
        try {
            while (currentLetter == null) {
                wait(); // Ici, si la boîte aux lettres est vide, le consommateur sera obligé de s'arrêter à son prochain essai de retrait.
            }
            String letter = currentLetter; // copie

            currentLetter = null;
            notify();       // Ici, on débloque le thread du producteur s'il est bloqué à l'instruction wait().

            System.out.println("LetterBox receive: " + letter);
            return letter;
        }
        catch(InterruptedException e) {
            e.printStackTrace();
            return "failed";
        }
    }
```  

Ici, la fonction `send()` bloque le thread du producteur si la boîte aux lettres contient déjà une lettre ; il y a une mise en attente dans la file. Le thread sera débloqué uniquement si la fonction `receive()` atteint le `notify()`.  
Tandis que la fonction `receive()` bloque le thread du consommateur si la boîte aux lettres est vide ; il ne peut pas récupérer de lettre lorsqu'il n'y en a pas.  
Le thread sera débloqué uniquement si la fonction `send()` atteint le `notify()`.  

Par la suite, on implémentera la possibilité de mettre fin aux threads avec la lettre "Q", et on ajoutera une interface graphique simple, ce qui nous permettra, en plus, d'écouter le clavier de l'utilisateur pour envoyer des lettres dans la file grâce à l'interface `KeyListener`.  

Voici le schéma UML final de la première partie de ce TP :  

![uml](res/tp3ulmschema.png)  

Cette première partie du TP3 nous apprend à implémenter un système de file d'attente (ou **Blocking Queue**), mais la deuxième partie nous apprendra à implémenter un fonctionnement plus avancé de ce système en utilisant, cette fois, des classes propres à Java.  

Dans la 2ᵉ partie du TP, la majorité de l'exercice tourne autour de la classe `ArrayBlockingQueue`.  
On retrouve le même fonctionnement que notre boîte aux lettres, mais cette fois-ci, il n'est pas question d'un seul élément (une lettre), mais bien d'une vraie file d'attente avec plusieurs éléments.  

Ici, les `consommateurs` seront remplacés par la classe `Mangeur`, les `producteurs` par la classe `Boulanger`, la boîte aux lettres par la classe `Boulangerie`, et les lettres par une simple classe `Pain`.  

Voici un extrait de la classe `Boulangerie`, qui est le cœur de la coordination des threads :  

```java
private BlockingQueue<Pain> queue =  new ArrayBlockingQueue<Pain>(20); // Voici la classe principalement visée par ce paradigme.

public boolean depose(Pain pain) throws InterruptedException {
    return queue.offer(pain, 200, TimeUnit.MILLISECONDS);
}

public Pain achete() throws InterruptedException {
    return queue.poll(200, TimeUnit.MILLISECONDS);
}

public int getStock() {
    return queue.size();
}
```  

Cette classe est une version modifiée de la classe bien connue `ArrayList`, qui permet de rajouter, supprimer ou modifier des éléments dans un tableau.  

Les particularités de cette classe sont les suivantes :  

- Cette classe utilise une structure de données en file, dite FIFO (First In, First Out).  
- Les fonctions d'ajout et de récupération (`offer()` & `poll()`) sont dites "bloquantes". Cela signifie que lorsque le nombre d'éléments dans la file dépasse le nombre maximal paramétré lors de l'instanciation de la classe, la fonction bloque le thread qui l'exécute jusqu'à ce qu'un autre thread libère de la place. Et inversement, lorsque la file est vide et qu'on essaye de récupérer un élément, la fonction bloque le thread jusqu'à ce que la file soit de nouveau remplie.  

C'est le principe de la classe `ArrayBlockingQueue`.  

Voici le schéma final de la seconde partie de ce TP3 :  

![uml](res/tp3blockingqueuesulmschema.png)  
