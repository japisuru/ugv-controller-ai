/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package genetic;

import java.io.Serializable;
import java.util.function.Function;

import org.jenetics.Chromosome;
import org.jenetics.DoubleGene;
import org.jenetics.Genotype;

import terrain.Position;
import terrain.PositionUtil;

/**
 *
 * @author isuru
 */

final class MyFitnessFunction implements Function<Genotype<DoubleGene>, Double>, Serializable {
	private static final long serialVersionUID = 1L;
	private Position[] neighborPositions;
	Position initialPosition;
	Position targetPosition;
	double vehicleRadius = 10.0;
	double maxNavDisPerStep = 10.0;
	int dimensions = 2;
	int numOfConstrains = 3;
	Position[] obstaclePositions;

	Position currentPosition;

	public MyFitnessFunction(Position currentPosition, Position[] neighborPositions, Position initialPosition,
			Position targetPosition, Position[] obstaclePositions) {
		this.neighborPositions = neighborPositions;
		this.initialPosition = initialPosition;
		this.targetPosition = targetPosition;
		this.currentPosition = currentPosition;
		this.obstaclePositions = obstaclePositions;
	}

	@Override
	public Double apply(final Genotype<DoubleGene> genotype) {
		Double retVal = 1000.0;

		final Chromosome<DoubleGene> chromosome = genotype.getChromosome();
		int length = chromosome.length();
		Position position = new Position(currentPosition.getX() + chromosome.getGene(0).getAllele(),
				currentPosition.getY() + chromosome.getGene(1).getAllele(), 0);

		// check inter agent collision
		for (int i = 0; i < neighborPositions.length; i++) {

			if (neighborPositions[i].getDistance(position) < vehicleRadius * 2.2) {
				return 0.0;
			} else {
				retVal = retVal + 1 / (((neighborPositions[i].getDistance(position))
						/ (numOfConstrains * 10 * (neighborPositions.length - 1))));
			}

		}

		// check obstacle collision
		for (int i = 0; i < obstaclePositions.length; i++) {

			if (obstaclePositions[i].getDistance(position) < vehicleRadius * 2.2) {
				return 0.0;
			} 
		}

		// check moving out from initial position
		retVal = retVal + (position.getDistance(initialPosition) / (numOfConstrains));

		// check check moving in to target position
		retVal = retVal - (position.getDistance(targetPosition) / (numOfConstrains));

		return retVal;

	}
}
