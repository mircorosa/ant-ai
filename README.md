# The Ant Game
The Ant Game is a simple example of how Artificial Intelligence can be used to solve problems.

## Concept
The main goal is to teach an Ant how to search food and not to go outside the board boundaries.\
This is accomplished by playing manually some games at first, in order to generate the training-set that the Ant will use to learn how to move; then this data-set is given in input to a classifier, that will build a model capable of deciding the direction to take based on the surroundings.

[Main Board](img/mainMenu.png)


Features:
### Training with user input
- Customization: board size, ant view radius, n° of games
- Automatic .arff file generation
- Full game logging
- Paths statistics
- Decision Tree AI
- Same games through multiple simulations

TBD:
- Random direction when space is empty?

TODO:
-License file

Authors

#
_This project has been developed for the "Artificial Intelligence" Master's Degree course @University of Parma._