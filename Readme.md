# Route Planer

## Summary

Router Planer is an android application which was built as an entree for the Code-Competition [Handlungsreisender](https://www.it-talents.de/foerderung/code-competition/code-competition-02-2017). The task for the challenge was to create an application which allows calculating an optimized route for given points. So basically the known Traveling salesman problem. Also, the application should at least be able to handle 10 route points.

## The solution

So the solution is an android application. Which uses the Google Places API for picking locations. In terms of route calculation, the geo-distance between the points are used. To solve the TSP I decided on using a combination of algorithms from the machine learning field.

For finding the optimal route between the given routes a genetic algorithm, hill climber algorithm and the simulated annealing algorithm get used. The combination of those three algorithms should allow for an optimized route each time, also the genetic algorithm allows for a better solution when there is a larger amount of route points to consider.


## Setup

This project is an basic android project, so after checking it out follow the known steps for importing an project into android studio.

## License

MIT