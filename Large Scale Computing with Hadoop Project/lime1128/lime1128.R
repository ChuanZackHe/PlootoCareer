# 11/28/2020,Sat

# install and library new packages to incorporate new functions
install.packages("rtweet")
library(rtweet)

#Retrieve twitter in the past 6 to 9 days
lime <- search_tweets(
  "limebike", n = 1800, include_rts = FALSE)

scooter <- search_tweets(
  "scooter", n = 1800, include_rts = FALSE)

brokenscooter <- search_tweets(
  "broken scooter", n = 1800, include_rts = FALSE)

limesafety <- search_tweets(
  "lime safety", n = 1800, include_rts = FALSE)

damagedscooter <- search_tweets(
  "damaged scooter", n = 1800, include_rts = FALSE)

write_as_csv(lime, "lime1128.csv")
write_as_csv(scooter, "scooter1128.csv")
write_as_csv(brokenscooter, "brokenscooter1128.csv")
write_as_csv(limesafety, "limesafety1128.csv")
write_as_csv(damagedscooter, "damagedscooter1128.csv")
