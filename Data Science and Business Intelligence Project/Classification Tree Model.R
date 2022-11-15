rm(list = ls())

data = read.csv("NYC_Restaurant_Data.csv")

library(tidyverse)
library(ggplot2)

summary(data)

data<-data%>%
  mutate(BORO=as.factor(BORO),
         ZIPCODE=as.factor(ZIPCODE),    
         CUISINE.ORIGIN=as.factor(CUISINE.ORIGIN),
         ACTION=as.factor(ACTION),
         VIOLATION=as.factor(VIOLATION),
         CRITICAL.FLAG=as.factor(CRITICAL.FLAG),
         GRADE=as.factor(GRADE))

data<-data%>%
  filter(CRITICAL.FLAG!="Not Applicable")%>%
  mutate(CRITICAL.FILTERED=as.factor(ifelse(CRITICAL.FLAG =="Critical","Yes","No")))

#data=data%>%
#    mutate(ACTION.CLEANED=as.factor(ifelse(ACTION=="No violations were recorded at the time of this inspection.","Safe","Not Safe")))

library(rpart)
library(rpart.plot)

# General Classification Tree Model
ct_model<-rpart(CRITICAL.FILTERED~BORO+CUISINE.ORIGIN+GRADE,           
                data=data,                      
                method="class",                    
                control=rpart.control(cp=0,maxdepth=5))

rpart.plot(ct_model)

# Training/Testing Split and Pruning

# Set up for holdout validation
# Select 20% of dataset. Using these indices, we will create a test and a training dataset. 
set.seed(1)   # set a random seed 
index <- sample(nrow(data), nrow(data)*0.2) # random selection of indices. 
test <- data[index,]       # save 20% as a test dataset
training <-data[-index,]   # save the rest as a training set

# Tree with training dataset
ct_model2<-rpart(CRITICAL.FILTERED~BORO+CUISINE.ORIGIN+GRADE,           
                data=training,                   
                method="class",                    
                control=rpart.control(cp=0,maxdepth=5))

rpart.plot(ct_model2)

printcp(ct_model2)

# Prune the tree using the cp value with the minimum xerror. Save the result as `min_xerror_tree`.
min_xerror_tree = ct_model2$cptable[which.min(ct_model2$cptable[,"xerror"]),]
min_xerror_tree = prune(ct_model2, cp=min_xerror_tree[1])

# Apply this model to the test dataset to get the predicted probabilities. 
bp_tree<-min_xerror_tree
test$ct_bp_pred_prob<-predict(bp_tree,test)[,2]

# Using the 50% cut-off, generate class prediction.  
test$ct_bp_pred_class=ifelse(test$ct_bp_pred_prob>0.6,"Yes","No")

# Error rate of this model when we use the 50% cut-off
table(test$ct_bp_pred_class==test$CRITICAL.FILTERED)  
sum(predict(bp_tree,test,type="class")!=test$CRITICAL.FILTERED)/nrow(test)

# Generate a confusion table of this model. What is the false positive rate of this model? 
table(test$ct_bp_pred_class,test$CRITICAL.FILTERED, dnn=c("predicted","actual")) 
12848/(21734+12848)  # HUGE false positive rate

# Performance Visualization with ROC
library(pROC)

ct_roc<-roc(test$CRITICAL.FILTERED,test$ct_bp_pred_prob,auc=TRUE)
plot(ct_roc,print.auc=TRUE)
