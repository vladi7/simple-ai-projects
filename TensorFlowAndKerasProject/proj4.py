import tensorflow as tf
from tensorflow import keras
import pandas as pd
import sys


set = pd.read_csv('heart.csv', na_values=['.'])
set = set.sample(frac=1)

#separating output from input
values_series = set['target']
x_set = set.pop('target')

#splitting the sets into 3
train_x_set = set[0:212]
train_y_set = x_set[0:212]
train_x_set = train_x_set.values
train_y_set = train_y_set.values

test_x_set = set[212:257]
test_y_set = x_set[212:257]
test_x_set = test_x_set.values
test_y_set = test_y_set.values

validation_x_set = set[257:]
validation_y_set = x_set[257:]
validation_x_set = validation_x_set.values
validation_y_set = validation_y_set.values


model = keras.Sequential()

#input with normalization
model.add(keras.layers.Dense(13, input_shape=(train_x_set.shape[1],)))
#model.add(tf.keras.layers.BatchNormalization())
model.add(tf.keras.layers.Activation('linear'))


#hidden layers
model.add(keras.layers.Dense(50))
model.add(tf.keras.layers.Activation('linear'))

model.add(keras.layers.Dense(20))
model.add(tf.keras.layers.Activation('linear'))

#output
model.add(keras.layers.Dense(1))
model.add(tf.keras.layers.Activation('sigmoid'))

model.summary()

model.compile(optimizer=tf.train.AdamOptimizer(),
              loss='binary_crossentropy',
              metrics=['accuracy'])

history = model.fit(train_x_set,
                    train_y_set,
                    epochs=40,
                    batch_size=256,
                    validation_data=(test_x_set, test_y_set),
                    verbose=1)


results = model.evaluate(validation_x_set, validation_y_set)


print(results)


history_dict = history.history
history_dict.keys()

import matplotlib.pyplot as plt

acc = history.history['acc']
val_acc = history.history['val_acc']
loss = history.history['loss']
val_loss = history.history['val_loss']

epochs = range(1, len(acc) + 1)

plt.plot(epochs, loss, 'ro', label='Training loss')
plt.plot(epochs, val_loss, 'r', label='Validation loss')
plt.title('Training and validation loss')
plt.xlabel('Epochs')
plt.ylabel('Loss')
plt.legend()

plt.show()

plt.clf()   

acc_values = history_dict['acc']
val_acc_values = history_dict['val_acc']

plt.plot(epochs, acc, 'ro', label='Training accuracy')
plt.plot(epochs, val_acc, 'r', label='Validation accuracy')
plt.title('Training and validation accuracy')
plt.xlabel('Epochs')
plt.ylabel('Accuracy')
plt.legend()

plt.show()
