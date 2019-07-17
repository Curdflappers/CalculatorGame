# Usage

This covers how to install and run the program, but not how to develop it.

## Installation

1. Clone the repository
1. Open the repository directory using Visual Studio Code. Make sure the Java Extension Pack enabled
1. Wait a bit for the project to get set up. After a few seconds, there should be feedback on the blue status bar at the bottom of the window, near the lower left. It should be something along the lines of "Refreshing Maven model", "XX% Starting Java Language Server", "Building workspace". Wait for this feedback to clear.

> Note: If there are any changes to `.classpath`, discard them. I am unsure why these changes occur, but it is likely related to [issue #859](https://github.com/redhat-developer/vscode-java/issues/859)

That's it! Everything should work once the project is fully loaded.

## Running in Linux

1. Click the area on the bottom status bar that says "▶ Main (calculator-game)" (Select and start debug configuration)
1. Choose `Main`

Tada! That was easy.

### Running in Windows

Follow the above steps for Linux. There may be a warning stating `Build failed, do you want to continue?`. Click `Proceed` and everything should work as expected. I am unsure why this warning appears.
