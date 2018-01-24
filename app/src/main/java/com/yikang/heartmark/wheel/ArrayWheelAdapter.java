/*
 *  Copyright 2010 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.yikang.heartmark.wheel;

import java.util.ArrayList;


/**
 * Numeric Wheel adapter.
 */
public class ArrayWheelAdapter implements WheelAdapter {
	
	private ArrayList<String> valueArray;
	public ArrayWheelAdapter(ArrayList<String> valueArray) {
		this.valueArray = valueArray;
	}

	@Override
	public String getItem(int index) {
			if (index >= 0 && index < valueArray.size()) {
				return valueArray.get(index);
			}
		return null;
	}

	@Override
	public int getItemsCount() {
		return valueArray.size();
	}

	@Override
	public int getMaximumLength() {
		return valueArray.size();
	}
	
}
