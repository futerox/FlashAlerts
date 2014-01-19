/***
 Copyright (c) 2013-2014 SpirosBond

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see http://www.gnu.org/licenses

 ***

 https://github.com/spirosbond/CallerFlashlight

 ***

 */


package com.futerox.flashlight;

import java.util.Comparator;

/**
 * Created by spiros on 9/5/13.
 */
public class SortByCheck implements Comparator {


	public int compare(Object o1, Object o2) {
		Model p1 = (Model) o1;
		Model p2 = (Model) o2;

		if ((p1.isSelected()) && (!p2.isSelected())) return -1;
		else if ((p2.isSelected()) && (!p1.isSelected())) return 1;
		else return 0;
	}
}
