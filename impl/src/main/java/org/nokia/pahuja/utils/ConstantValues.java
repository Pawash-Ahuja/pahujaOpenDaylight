package org.nokia.pahuja.utils;

import java.util.HashSet;

public class ConstantValues {

	static HashSet<Integer> allVlans = new HashSet<Integer>();

	public static HashSet<Integer>  getAllVlans(){

		if (allVlans.isEmpty()){

			for (int i = 1; i <= 125; i++){

				allVlans.add(i);
			}
		}
		return allVlans;

	}

}
