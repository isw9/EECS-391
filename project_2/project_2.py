import matplotlib.pyplot as plt
import pandas as pd
import seaborn as sns
import numpy as np
from sklearn import model_selection
from sklearn import datasets
import random
from keras.layers import Dense, Activation
from keras.models import Sequential, Model
from array import array
from matplotlib import cm
from mpl_toolkits.mplot3d import Axes3D
from matplotlib.ticker import LinearLocator, FormatStrFormatter

def modelNN(input_dimension):
    model = Sequential()
    model.add(Dense(1,input_dim=input_dimension,activation='sigmoid'))
    model.compile(optimizer='rmsprop',loss='mean_squared_error',metrics=['accuracy'])
    return model

def sigmoid(z):
    return 1 / (1 + np.exp(-z))

def get_line_coords(weight_one, weight_two, bias):
    x = -bias / weight_one
    y = -bias / weight_two
    d = y
    c = -y / x
    line_x_coords = [0, x]
    line_y_coords = []
    for i in range(2):
        line_y_coords.append(c * line_x_coords[i] + d)

    result_coords = [line_x_coords, line_y_coords]
    return result_coords

def derivativeSigmoid(z):
    #sigmoid_result = sigmoid(z)
    return z * (1 - z)

def mean_squared_error(dataset, target, weight_one, weight_two, bias, learning_rate):

    squared_error_running_total = 0.0
    squared_error_normalized = 0.0
    weight_one_update = 0.0
    weight_two_update = 0.0
    bias_update = 0.0

    size = len(dataset)
    for i in range(size):
        z = dataset[i][0]* weight_one + dataset[i][1] * weight_two + bias
        sigmoid_result = sigmoid(z)
        derivative_result = derivativeSigmoid(sigmoid_result)
        if (target[i] == 'versicolor'):
            actual_class = 0
        else:
            actual_class = 1

        err_result = (sigmoid_result - actual_class)
        weight_one_update = weight_one_update + (err_result * derivative_result * dataset[i][0])
        weight_two_update = weight_two_update + (err_result * derivative_result * dataset[i][1])
        bias_update = bias_update + (err_result * derivative_result)
        squared_error_result = err_result ** 2
        squared_error_running_total += squared_error_result

    new_weight_one = weight_one - (learning_rate * weight_one_update)
    new_weight_two = weight_two - (learning_rate * weight_two_update)
    new_bias = bias - (learning_rate * bias)

    squared_error_normalized = (squared_error_running_total / size)
    return_array = [squared_error_normalized, new_weight_one, new_weight_two, new_bias]
    return return_array

data = pd.read_csv('irisdata.csv', sep=',')

def problem_1_a(problem_letter, x_coords_line, y_coords_line):
    colors = ['red', 'blue']
    species = ['setosa', 'virginica', 'versicolor']
    for i in range(1, 3):
        species_df = data[data['species'] == species[i]]
        plt.scatter(species_df['petal_length'],species_df['petal_width'],color=colors[i-1])

    plt.xlabel('petal length (cm)')
    plt.ylabel('petal width (cm)')
    plt.title('Petal Width vs Petal Length - Problem #1(A)')
    if (problem_letter == 'c'):
        plt.plot(x_coords_line, y_coords_line)
        plt.title('Petal Width vs Petal Length - Problem #1(C)')
    elif (problem_letter == '2_e_beginning'):
        plt.plot(x_coords_line, y_coords_line)
        plt.title('Petal Width vs Petal Length - Problem #2(E) before small step')
    elif (problem_letter == '2_e_end'):
        plt.plot(x_coords_line, y_coords_line)
        plt.title("Petal Width vs Petal Length - Problem #2(E) after small step")

    plt.show()

def problem_1_b(problem_letter):
    dataset = data.iloc[50:, [2,3]].values
    target = data.iloc[50:, 4].values

    x_train, x_test, y_train, y_test = model_selection.train_test_split(dataset, target, test_size = 0.4)

    #initialize the two weights and bias
    weight_one = 0.48
    weight_two = 0.99
    bias = - 3.9


    size = len(x_train)
    for i in range(size):
        z = x_train[i][0]* weight_one + x_train[i][1] * weight_two + bias
        sigmoid_result = sigmoid(z)
        if (y_train[i] == 'versicolor'):
            actual_class = 0
        else:
            actual_class = 1

    if (problem_letter == 'e'):
        x_test = [[4.8, 1.8], [5.1, 1.8], [5.1, 2], [5, 1.9], [5.1, 1.9]]
        y_test = ['versicolor', 'virginica', 'virginica', 'viginica', 'versicolor'  ]

    if (problem_letter == 'ee'):
        x_test = [[4, 1.3], [4.6, 1.5], [3.3, 1], [3.9, 1.4], [6.6, 2.1], [6.3, 1.8], [6.1, 2.5], [6.7, 2.2]]
        y_test = ['versicolor', 'versicolor', 'versicolor', 'versicolor', 'virginica', 'virginica', 'virginica', 'virginica']
    size = len(x_test)
    num_wrong = 0
    num_right = 0
    for i in range(size):
        if (y_test[i] == 'versicolor'):
            actual_class = 0

        else:
            actual_class = 1

        z = x_test[i][0] * weight_one + x_test[i][1] * weight_two + bias
        sigmoid_result = sigmoid(z)
        if (sigmoid_result < 0.5):
            predicted_class = 0
        else:
            predicted_class = 1

        print("actual class: ", actual_class)
        print("predicted class: ", predicted_class)
        if (actual_class != predicted_class):
            num_wrong += 1
        else:
            num_right += 1
        print("--------")
    percent_correct = num_right / (num_right + num_wrong)
    if (problem_letter == 'e'):
        print("part 1e-amount correct when looking near the decision boundary: ", percent_correct)
        print('')
        print('')
    elif (problem_letter == 'ee'):
        print("part 1e-amount correct when looking far from the decision boundary: ", percent_correct)
        print('')
        print('')
    else:
        print("part 1b-amount correct: ", percent_correct)
        print('')
        print('')

        problem_1_c(weight_one, weight_two, bias)
        problem_1_d(dataset, target, weight_one, weight_two, bias)

def problem_1_c(weight_one, weight_two, bias):
    line_coords = get_line_coords(weight_one, weight_two, bias)
    line_x_coords = line_coords[0]
    line_y_coords = line_coords[1]
    problem_1_a('c', line_x_coords, line_y_coords)

def problem_1_d(dataset, target, weight_one, weight_two, bias):
    sigmoidValues = []
    petal_length_values = []
    petal_width_values = []
    size = len(dataset)
    for i in range(size):
        z = dataset[i][0]* weight_one + dataset[i][1] * weight_two + bias
        sigmoid_result = sigmoid(z)
        sigmoidValues.append(sigmoid_result)
        petal_length_values.append(dataset[i][0])
        petal_width_values.append(dataset[i][1])

    petal_length_values = np.array(petal_length_values)
    x = np.reshape(petal_length_values, (10, 10))
    y = np.reshape(petal_width_values, (10, 10))
    z = np.reshape(sigmoidValues, (10, 10))

    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')

    ax.plot_surface(x, y, z)

    ax.set_xlabel('petal length - cm')
    ax.set_ylabel('petal width - cm')
    ax.set_zlabel('sigmoid value')

    plt.show()

def problem_1_e():
    problem_1_b('e')
    problem_1_b('ee')

def problem_2_b():
    dataset = data.iloc[50:, [2,3]].values
    target = data.iloc[50:, 4].values
    good_parameters = [.48, .99, -3.9]
    bad_parameters = [.99, .98, 1.2]

    good_mean_squared = mean_squared_error(dataset, target, good_parameters[0], good_parameters[1], good_parameters[2], .01)
    bad_mean_squared= mean_squared_error(dataset, target, bad_parameters[0], bad_parameters[1], bad_parameters[2], .01)

    print("Good parameters mean squared error: ", good_mean_squared[0])
    print("Bad parameters mean squared error: ", bad_mean_squared[0])

def problem_2_e():
    dataset = data.iloc[50:, [2,3]].values
    target = data.iloc[50:, 4].values
    weight_one = .99
    weight_two = .98
    bias = -3.2

    line_coords = get_line_coords(weight_one, weight_two, bias)
    line_x_coords = line_coords[0]
    line_y_coords = line_coords[1]
    problem_1_a('2_e_beginning', line_x_coords, line_y_coords)

    mean_squared_result = mean_squared_error(dataset, target, weight_one, weight_two, bias, .001)
    update_results = mean_squared_result
    for i in range(5):
        mean_squared_result = mean_squared_error(dataset, target, update_results[1], update_results[2], update_results[3], .001)
        update_results = mean_squared_result

    line_coords = get_line_coords(update_results[1], update_results[2], update_results[3])
    line_x_coords = line_coords[0]
    line_y_coords = line_coords[1]
    problem_1_a("2_e_end", line_x_coords, line_y_coords)


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
    problem_1_a('a', [], [])
    problem_1_b('b')
    problem_1_e()
    problem_2_b()
    problem_2_e()
    input("Enter an integer to continue. I stopped here so you can look at my terminal results to problems 1, 2, and 3 before problem 4 completely fills up the screen ")
    problem_4_a()
    problem_4_b()
