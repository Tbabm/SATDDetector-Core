# SATD Detector Core
## Introduction
SATD refers to self-admitted technical debt, which is introduced intentionally (e.g., through temporary fix) and admitted by developers themselves and always recorded in source code comments. SATD Detector[1] is a tool that is able to automatically detect SATD comments text mining. This is the back-end of SATD Detector, which provides command-line interface and Java API of SATD Detector. More details can be found in [1].

## Play with pre-build binaries
Download binaries: [satd_detector.jar](https://github.com/Tbabm/SATDDetector-Core/releases/tag/v0.1)


### Test and play
Use build-in models to test whether a comment is SATD comment or not:

```bash
$ java -jar satd_detector.jar test
>This is an ugly implementation.
SATD
>This function read lines from file.
Not SATD
>/exit
bye!
```

### Train your own models
To train your own models, you need provide three data files:

- Comment file: contains all the **comments** in your dataset. One line for each comment.
- Label file: contains the **labels** of comments.
- Project file: contains the **project names** of comments.

Comment file example: comments.txt

```txt
// This is comment 1
// TODO: fix this function later
/* This is comment 3 */
```

Label file example: labels.txt

```txt
No
Yes
No
```

Project file example: projects.txt

```txt
project1
project2
project3
```

Train your own model

```bash
$ java -jar satd_detector.jar train -comments comments.txt -labels labels.txt -projects projects.txt -out_dir ./models/
Finish Saving classifier of project1
Finish Saving classifier of project2
Finish Saving classifier of project3
```

Now you can see 9 files in `./models/`:

```bash
- models
    - project1.as
    - project1.model
    - project1.stw
    - project2.as
    - project2.model
    - project2.stw
    - project3.as
    - project3.model
    - project3.stw
```

All these files will be loaded while classifying.

### Test and play with your own models
```bash
$ java -jar satd_detector.jar test -model_dir ./models/
>This is an ugly implementation.
SATD
>This function read lines from file.
Not SATD
>/exit
bye!
```

## Java API
Your can also use SATD Detector in your Java projects. 

- Download satd_detector.jar
- Add it to the classpath

```Java
import satd_detector.core.utils.SATDDetector;

// create an instance using build-in models
SATDDetector detector1 = new SATDDetector();
String comment = "This is an ugly implementation."
boolean result = detector1.isSATD(comment)

// create an instance using your own models
String modelDir = "./models/"
SATDDetector detector2 = new SATDDetector(modelDir);
boolean result = detector2.isSATD(comment)
```

## Other Info
```bash
$ java -jar satd_detector.jar test -h
usage: test
 -h                 Show help message
 -model_dir <arg>   Dir which stores all the models. Using build-in models
                    if not specified.

$ java -jar satd_detector.jar train -h
usage: train
 -comments <arg>   The file which stores all comments.
 -h                Show help messages.
 -labels <arg>     The file which stores labels of all comments.
 -out_dir <arg>    Dir to store trained models.
 -projects <arg>   The file which stores projects of all comments.
```

## Build from source code
### Requirements
- weka 3.8.1 or above
- snowball-stemmers
    - which can be installed using weka package manager
- Apache commons cli 1.4 or above

### Steps
- Git clone or download zip
- Add `resources` folder to your classpath
- Add `weka.jar`, `snowball-stemmers.jar` and `commons-cli.jar` to your classpath

For Eclipse, you can modify classpath quickly by:

- Righ-click on the project
- Choose `Build Path` -> `Configure Build Path`
    - for `resources` folder, choose `Source` tab -> Add Folder
    - for jar files, choose `Libraries` tab -> Add External JARs

## References
[1] SATD Detector: A Text-Mining-Based Self-Admitted Technical Debt Detection Tool.



