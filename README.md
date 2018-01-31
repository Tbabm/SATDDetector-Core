## SATD Detector backend
### How to deploy
- Git clone
- Add resources folder to your classpath
    - For Eclipse, create a project
    - build path -> configure build path -> source -> add folder
    - Add resources folder
- Add weka and snowball-stemmers to your classpath
    - For Eclipse, create a project
    - build path -> configure build path -> libraries -> add external jars
    - add weka and snowball-stemmers

### How to use jar
#### For test and fun
```bash
# use default models to test
$ java -jar satd_detector.jar test

# use new user-defined models to test
$ java -jar satd_detector.jar test -model_dir ./models/
```

#### For train
```bash
# train new models use you own data
$ java -jar satd_detector.jar train -comments comments_file -labels labels_file -projects projects_file -out_dir ./models/
```

#### API Usage
```Java
import satd_detector.core.utils.SATDDetector;

// create an instance using build-in models
SATDDetector detector1 = new SATDDetector();
String comment = "This is an ugly implementation."
boolean result = detector1.isSATD(comment)

// create an instance using user's models
SATDDetector detector2 = new SATDDetector(modelDir);
boolean result = detector2.isSATD(comment)
```





