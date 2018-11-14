import matplotlib.pyplot as plt
import pandas as pd
import seaborn as sns


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

#dataset.apply(pd.to_numeric, errors='ignore')
#print(dataset.petal_width)
#print(dataset.petal_width.dtype)


#sns.regplot(x=dataset["petal_length"], y=dataset["petal_width"])
plt.scatter(petal_length, petal_width)
plt.xlabel("Petal Length (cm) ")
plt.ylabel("Petal Width (cm) ")
plt.show()
