javac -classpath lib/ModernMultithreading.jar src/main/java/Homework2Problem3.java
#java -classpath lib/ModernMultithreading.jar:src/main/java -Dmode=rt -DdeadlockDetection=on RTDriver Homework2Problem3
java -classpath lib/ModernMultithreading.jar:src/main/java -Dmode=rt -DdeadlockDetection=on -DVPReduction=on -DsymmetryReduction=on RTDriver Homework2Problem3
