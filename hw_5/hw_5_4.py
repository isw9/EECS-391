import matplotlib.pyplot as plt
import pandas as pd
import seaborn as sns
import numpy as np
import random

def dist(data_point, centroids, k):
    distances = []
    for i in range(k):
        temp_centroid = centroids[i]
        temp_dist = np.linalg.norm(data_point-temp_centroid)
        distances.append(temp_dist)
    return distances


names = ['sepal_length', 'sepal_width', 'petal_length', 'petal_width', 'species']

dataset = pd.read_csv("irisdata.csv", names=names)
dataset = dataset.iloc[1:]
sepal_length = dataset["sepal_length"].values
sepal_width = dataset["sepal_width"].values
petal_length = dataset["petal_length"].values
petal_width = dataset["petal_width"].values

for index, item in enumerate(sepal_length):
    sepal_length[index] = float(item)

for index, item in enumerate(sepal_width):
    sepal_width[index] = float(item)

for index, item in enumerate(petal_length):
    petal_length[index] = float(item)

for index, item in enumerate(petal_width):
    petal_width[index] = float(item)

data = np.array(list(zip(petal_length, petal_width)))

for k in range(2, 4):
    c_petal_length =  []
    c_petal_width =  []
    for i in range(k):
        choice = random.choice(petal_length)
        c_petal_length.append(choice)

        choice = random.choice(petal_width)
        c_petal_width.append(choice)

    plt.scatter(petal_length, petal_width)
    plt.xlabel('petal length (cm)')
    plt.ylabel('petal width (cm)')
    plt.title("beginning")
    plt.scatter(c_petal_length, c_petal_width, marker='*', s=200, c='g')
    plt.show()


    centroids = np.array(list(zip(c_petal_length, c_petal_width)))

    if (k == 3):
        old_centroids = [[0.0, 0.0], [0.0, 0.0], [0.0, 0.0]]
    else:
        old_centroids = [[0.0, 0.0], [0.0, 0.0]]

    data_point_clusters = []
    for i in range(150):
        data_point_clusters.append(0)

    centroid_error = np.linalg.norm(centroids - old_centroids, axis=1)

    time = 0
    while centroid_error.any() > 0.0001:
        for i in range(150):
            distances = dist(data[i], centroids, k)
            cluster_number = np.argmin(distances)
            data_point_clusters[i] = cluster_number

        old_centroids = centroids
        # find the new centroids
        zero_cluster = [0.0, 0.0]
        num_zero = 0
        one_cluster = [0.0, 0.0]
        num_one = 0
        two_cluster = [0.0, 0.0]
        num_two = 0
        for j in range(150):
            if data_point_clusters[j] == 0:
                zero_cluster[0] = zero_cluster[0] + data[j][0]
                zero_cluster[1] = zero_cluster[1] + data[j][1]
                num_zero += 1
            elif data_point_clusters[j] == 1:
                one_cluster[0] = one_cluster[0] + data[j][0]
                one_cluster[1] = one_cluster[1] + data[j][1]
                num_one += 1
            elif data_point_clusters[j] == 2:
                two_cluster[0] = two_cluster[0] + data[j][0]
                two_cluster[1] = two_cluster[1] + data[j][1]
                num_two += 1
        if num_zero > 0:
            zero_cluster[0] = zero_cluster[0] / num_zero
            zero_cluster[1] = zero_cluster[1] / num_zero

        if num_one > 0:
            one_cluster[0] = one_cluster[0] / num_one
            one_cluster[1] = one_cluster[1] / num_one

        if num_two > 0:
            two_cluster[0] = two_cluster[0] / num_two
            two_cluster[1] = two_cluster[1] / num_two

        if (k == 3):
            centroids = [[zero_cluster[0], zero_cluster[1]], [one_cluster[0], one_cluster[1]], [two_cluster[0], two_cluster[1]]]
        else:
            centroids = [[zero_cluster[0], zero_cluster[1]], [one_cluster[0], one_cluster[1]]]

        centroids = np.array(centroids)
        centroid_error = np.linalg.norm(centroids - old_centroids, axis=1)
        time += 1

        if time == 1:
            new_c_petal_length = []
            new_c_petal_width = []
            for j in range(k):
                new_c_petal_length.append(centroids[j][0])
                new_c_petal_width.append(centroids[j][1])
            plt.scatter(petal_length, petal_width)
            plt.xlabel('petal length (cm)')
            plt.ylabel('petal width (cm)')
            plt.title("intermediate")
            plt.scatter(new_c_petal_length, new_c_petal_width, marker='*', s=200, c='g')
            plt.show()
    final_c_petal_length = []
    final_c_petal_width = []
    for j in range(k):
        final_c_petal_length.append(centroids[j][0])
        final_c_petal_width.append(centroids[j][1])
    plt.scatter(petal_length, petal_width)
    plt.xlabel('petal length (cm)')
    plt.ylabel('petal width (cm)')
    plt.title("final")
    plt.scatter(final_c_petal_length, final_c_petal_width, marker='*', s=200, c='g')
    plt.show()
