mvn package
scp -r conf root@sj1.cn:/data/apps/learningEnglish
scp target/EngReader-0.0.1-SNAPSHOT.jar root@sj1.cn:/data/apps/learningEnglish
