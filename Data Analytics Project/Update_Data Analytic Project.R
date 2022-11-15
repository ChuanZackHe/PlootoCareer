library(dplyr)
library(rpart)
library(pROC)
library(ggplot2)
library(rattle)
library(caret)
library(rpart.plot)
library(nnet)



data1 <- read.csv("data.csv",na.strings = "?")
summary(data1)

library(tidyverse)

## Changing data type for dependent variable
data1$Bankrupt. <- as.factor(data1$Bankrupt.)
summary(data1$Bankrupt.)
## Splitting into training and validation dataset
data_training <- data1[c(1:6500),]
data_validation <- data1[c(6501:6819),]


## Model 1: Subset selection
library(ISLR)
library(leaps)

# regfit.full=regsubsets(Bankrupt.~., data = data_training,nvmax=20,really.big=T)
# summary(regfit.full)$adjr2

regfit.forward=regsubsets(Bankrupt.~., data = data_training,nvmax=50,really.big=T,method="forward")
summary(regfit.forward)$adjr2

regfit.backward=regsubsets(Bankrupt.~., data = data_training,nvmax=20,really.big=T,method="backward")
summary(regfit.backward)$adjr2

## Model 2 Logistic Regression
fullreg <- glm(Bankrupt.~.,data=data_training,family = "binomial")
nullreg <- glm(Bankrupt.~1,data = data_training,family = "binomial")
model1 <- step(nullreg, list(lower = formula(nullreg), upper = formula(fullreg)), 
               data = data_training, direction = "both", trace = 0)
f1 <- formula(model1)

model2 <- glm(Bankrupt.~(Persistent.EPS.in.the.Last.Four.Seasons + Debt.ratio.. + 
                           Net.Worth.Turnover.Rate..times. + Cash.Total.Assets + Net.Value.Per.Share..B. + 
                           Net.Value.Per.Share..C. + Cash.Turnover.Rate + Cash.Flow.to.Liability + 
                           Accounts.Receivable.Turnover + Operating.Profit.Per.Share..Yuan.?.. + 
                           Fixed.Assets.Turnover.Frequency + Total.debt.Total.net.worth + 
                           Per.Share.Net.profit.before.tax..Yuan.?.. + Fixed.Assets.to.Assets + 
                           Inventory.and.accounts.receivable.Net.value + Current.Assets.Total.Assets + 
                           Borrowing.dependency + ROA.C..before.interest.and.depreciation.before.interest + 
                           Cash.Reinvestment.. + Liability.to.Equity + Cash.Flow.Per.Share + 
                           Liability.Assets.Flag + Working.Capital.Equity + Operating.Profit.Rate + 
                           Continuous.interest.rate..after.tax. + Cash.flow.rate + Quick.Ratio)^2,
                           data = data_training,family = "binomial")
summary(model2)

## Model 3: knn Classification
library(class)
attach(data1)
set.seed(1)

train = sample(1:nrow(data1), nrow(data1)/2)

company.train = data1[train,-1]
company.test = data1[-train,-1]

Bankrupt.train = Bankrupt.[train]
Bankrupt.test = Bankrupt.[-train]

knn.pred.1 = knn(company.train,company.test,Bankrupt.train,k=1)
table(knn.pred.1,Bankrupt.test)
mean(knn.pred.1==Bankrupt.test)

knn.pred.2 = knn(company.train,company.test,Bankrupt.train,k=2)
table(knn.pred.2,Bankrupt.test)
mean(knn.pred.2==Bankrupt.test)

knn.pred.3 = knn(company.train,company.test,Bankrupt.train,k=3)
table(knn.pred.3,Bankrupt.test)
mean(knn.pred.3==Bankrupt.test)

## Model 4: Decision Tree

tree1 <- rpart(Bankrupt.~.,data= data1,control = rpart.control(maxdepth=5,cp=0.0001))
rpart.plot(tree1)
prp(tree1)






