/**
 * Copyright (C) 2009, 2010 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tek.first.livingbetter.wallet;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import tek.first.livingbetter.wallet.model.ItemModel;

public interface IChart {
  String NAME = "name";
  String DESC = "desc";

  String getName();

  String getDesc();

  Intent execute(Context context, ArrayList<ItemModel> item);
}
