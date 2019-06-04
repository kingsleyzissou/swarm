# swarm
Bird flock analyser

A JavaFX application that can be supplied with an image/photo of a flock of birds in the sky. The application takes in an image,
filters out noise and then locates birds and clusters of birds to estimate the number of birds in the image. The key aspect of the project
was to use a custom disjoint set data structure and union-find algoritm.

Using linear regression in four different detections to detect whether or not a flock is flying in a cluster, 
a single-line formation or in a v-formation.
