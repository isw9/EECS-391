import matplotlib.pyplot as plt
import pandas as pd
import seaborn as sns
import numpy as np
from sklearn import model_selection
from sklearn import datasets
import random
from keras.layers import Dense, Activation
from keras.models import Sequential, Model

def modelNN(input_dimension):
    model = Sequential()
    model.add(Dense(1,input_dim=input_dimension,activation='sigmoid'))
    model.compile(optimizer='rmsprop',loss='mean_squared_error',metrics=['accuracy'])
    return model

def sigmoid(z):
    return 1 / (1 + np.exp(-z))

data = pd.read_csv('irisdata.csv', sep=',')

def problem_1_a():
    colors = ['red', 'blue']
    species = ['setosa', 'virginica', 'versicolor']
    for i in range(1, 3):
        species_df = data[data['species'] == species[i]]
        plt.scatter(species_df['petal_length'],species_df['petal_width'],color=colors[i-1])

    plt.xlabel('petal length (cm)')
    plt.ylabel('petal width (cm)')
    plt.title('Petal Width vs Petal Length - Classes 2 and 3')
    plt.show()

def problem_1_b():
    random.seed(123) #for debugging
    dataset = data.iloc[50:, [2,3]].values
    target = data.iloc[50:, 4].values
    #seed the random_state so we get the same result on each pass for debugging purposes
    x_train, x_test, y_train, y_test = model_selection.train_test_split(dataset, target, test_size = 0.4, random_state=10)

    weight_one = random.random
    weight_two = random.random
    bias = random.random
    error = []
    n = 0
    p_n = 0

    size = x_train.size
    print(size)
    for i in range(size):
        #?????

def problem_4_a():

    encodeOutput = []
    Input = data.iloc[50:, [2,3]].values
    Output = data.iloc[50:, 4].values
    for i in Output:
        if i == 'versicolor':
            encodeOutput.append(0)
        else:
            encodeOutput.append(1)

    inputTrain, inputVal, outputTrain, outputVal = model_selection.train_test_split(Input,encodeOutput,test_size=0.25,shuffle=True)
    model = modelNN(2)
    model.fit(x=inputTrain,y=outputTrain,epochs=2000, validation_data=(inputVal,outputVal))

def problem_4_b():
    encodeOutput = []
    Input = data.iloc[0:, [0, 1, 2, 3]].values
    Output = data.iloc[0:, 4].values

    for i in Output:
        if i == 'setosa':
            encodeOutput.append(0)
        elif i == 'versicolor':
            encodeOutput.append(1)
        else:
            encodeOutput.append(2)

    inputTrain, inputVal, outputTrain, outputVal = model_selection.train_test_split(Input,encodeOutput,test_size=0.25,shuffle=True)
    model = modelNN(4)
    model.fit(x=inputTrain,y=outputTrain,epochs=2000, validation_data=(inputVal,outputVal))


if __name__ == "__main__":
    problem_1_a()
    #problem_1_b()
    problem_4_a()
    problem_4_b()
