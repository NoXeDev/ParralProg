import numpy as np
import matplotlib.pyplot as plt
import subprocess
import time
import statistics

DATA_REDUDENCY = 5
colors = ["blue", "red", "green", "orange", "purple", "brown", "pink", "black", "cyan", "magenta", "yellow"]

def generate_data_scal_forte(iterations, proc, classname, filename):
    """Exécute le programme Java plusieurs fois pour assurer des résultats cohérents."""
    for p in range(1, proc+1):  # De 1 à 16 processus
        for _ in range(DATA_REDUDENCY):
            print(["java", classname, str(int(iterations)), str(p), filename])
            subprocess.run(["java", "-cp", ".\\TP4_Montecarlo\\", classname, str(int(iterations/p)), str(p), filename], stdout=subprocess.DEVNULL)

def generate_data_scal_faible(iterations, proc, classname, filename):
    """Exécute le programme Java plusieurs fois pour assurer des résultats cohérents."""
    for p in range(1, proc+1):  # De 1 à 16 processus
        for _ in range(DATA_REDUDENCY):
            print(["java", classname, str(int(iterations)), str(p), filename])
            subprocess.run(["java", "-cp", ".\\TP4_Montecarlo\\", classname, str(int(iterations)), str(p), filename], stdout=subprocess.DEVNULL)

def lire_donnees(fichier):
    """Lit un fichier contenant les données d'exécution et calcule le speedup."""
    data = []
    temps_premiere = 0
    time_buff = []
    
    with open(fichier, 'r') as f:
        for line in f:
            if line.strip() and not line.startswith("#"):  # Ignorer les lignes vides et commentaires
                parts = line.split(',')
                nb_processeurs = int(parts[2].strip())
                temps_execution = int(parts[3].strip())

                if len(time_buff) >= DATA_REDUDENCY:
                    if len(data) == 0:
                        data.append([nb_processeurs, 1])
                        temps_premiere = statistics.median(time_buff)
                    else:
                        speedup = temps_premiere / statistics.median(time_buff)
                        data.append([nb_processeurs, speedup])
                    time_buff = [] # flush buffer
                else :
                    time_buff.append(temps_execution)
    
    return np.array(data)

def tracer_scalabilite(data_forte, data_faible):
    """Trace les graphiques de scalabilité forte et faible avec courbe attendue."""
    plt.figure(figsize=(12, 5))
    
    # Scalabilité Forte
    plt.subplot(1, 2, 1)
    x_expected = np.linspace(1, 16, 100)
    y_expected = x_expected  # Croissance linéaire idéale
    plt.plot(x_expected, y_expected, label="Speedup attendu", linestyle="-", color="gray")
    
    for i, (k, datafort) in enumerate(data_forte.items()):
        x_forte = datafort[:, 0]  # Nombre de processus
        y_forte = datafort[:, 1]  # Speedup
        plt.plot(x_forte, y_forte, label=str(k) + " itérations", linestyle="-", marker='o', color=colors[i])

    plt.xlabel("Nombre de Processus")
    plt.ylabel("Speedup")
    plt.title("Scalabilité Forte")
    plt.legend()
    plt.grid()
    
    # Scalabilité Faible
    plt.subplot(1, 2, 2)
    x_expected_faible = np.linspace(1, 16, 100)
    y_expected_faible = np.ones_like(x_expected_faible)
    plt.plot(x_expected_faible, y_expected_faible, label="Speedup attendu", linestyle="-", color="gray")

    for i, (k, datafaible) in enumerate(data_faible.items()):
        x_faible = datafaible[:, 0]
        y_faible = datafaible[:, 1]
        plt.plot(x_faible, y_faible, label=str(k) + " itérations", linestyle="-", marker='o', color=colors[i])

    plt.xlabel("Nombre de Processus")
    plt.ylabel("Speedup")
    plt.title("Scalabilité Faible")
    plt.legend()
    plt.grid()
    
    plt.show()

# Exécution du programme
generate_data_scal_forte(10000000, 16, "Pi", "out_pi_fort10000000.txt")
generate_data_scal_faible(10000000, 16, "Pi", "out_pi_faible10000000.txt")
generate_data_scal_forte(100000000, 16, "Pi", "out_pi_fort100000000.txt")
generate_data_scal_faible(100000000, 16, "Pi", "out_pi_faible100000000.txt")

#generate_data_scal_faible(12_000_000, 16, "Assignment102", "../out_ass_fort12_000_000.txt")
#generate_data_scal_faible(12_000_000, 16, "Assignment102", "../out_ass_faible12_000_000.txt")

data1 = lire_donnees("./out_pi_fort10000000.txt")
data2 = lire_donnees("./out_pi_fort100000000.txt")
scale_forte = {
    10000000: data1,
    100000000: data2
}

data1 = lire_donnees("./out_pi_faible10000000.txt")
data2 = lire_donnees("./out_pi_faible100000000.txt")
scale_faible = {
    10000000: data1,
    100000000: data2
}

tracer_scalabilite(scale_forte, scale_faible)
