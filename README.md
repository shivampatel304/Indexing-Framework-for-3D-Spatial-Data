# Indexing-Framework-for-3D-Spatial-Data

This project involves developing an effective index structure to speed up finding answers to two types of queries on a dataset of 10 million points in the 3D space stored in relation Points(X,Y,Z), where the attributes are real numbers in the range [0, 1000]. The first type of query involves finding all the points in the input dataset that are inside or lie on the borders of the cube defined by query parameters x1, x2, y1, y2, z1, and z2. The second type of query involves finding the nearest neighbor point(s) of a given point A(x1, y1, z1) in the dataset.

The index structure can be either standard or ad hoc, tree-based or hash-based, and should be used for both types of queries. The project report should present the index/hash structure or a mixed (ad hoc) solution and explain its features. Additionally, the report should include the size of the index created and the query processing time to answer the queries. The maximum number of points for answering each query type is three, and the maximum points for the report are two. The presentation style and Q/A during the demo will get one point at most.

The goal of this project is to build a fast and scalable indexing structure for 3D point clouds, using advanced indexing techniques to accelerate spatial query processing.
