lime_reduce<-data.frame(lime1128$text)
names(lime_reduce)<-"text"
lime_reduce[,"tweet_id"] <-c(1:358)
lime_reduce[,"company"] <-"lime"
lime_reduce<-lime_reduce[c("tweet_id","company","text")]

write.csv(lime_reduce,"Tweets.csv",row.names=FALSE)

