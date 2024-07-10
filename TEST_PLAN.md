# Test Plan and Results

## 1. Treasure Chest Creation

### Test Case 1.1: Empty Database Creation

- **Objective**: Verify that treasure chests can be created with an empty database.
- **Steps**:
    1. Place a chest.
    2. Stand next to it, with the cross-hair pointing at the chest.
    3. Run the command `/treasurehunt addchest MyTreasureChest`
- **Expected Result:** A treasure chest with ID **MyTreasureChest** is created.
- **Actual Result:** The treasure chest was successfully created.
- **Status:** PASS.

### Test Case 1.2: Existing Treasure Chest ID Creation

- **Objective**: Confirm that the plugin can detect when a treasure chest ID is already used by another treasure chest.
- **Steps**:
    1. Create a treasure chest with ID **MyTreasureChest**.
    2. Attempt to create a second treasure chest using the same ID, **MyTreasureChest**.
- **Expected Result:** The plugin detects another treasure chest has the ID **MyTreasureChest** and the treasure chest
  creation fails.
- **Actual Result:** The plugin recognized the ID is already used and notified the player, failing to create the
  treasure chest.
- **Status:** PASS.

### Test Case 1.3: Treasure Chest Re-utilization

- **Objective**: Verify that the plugin doesn't allow to create two treasure chests on the same chest block.
- **Steps**:
    1. Create a treasure chest with ID **MyTreasureChest**.
    2. Without placing another chest, and while looking at the same chest, attempt to create another treasure chest with
       ID **MyOtherTreasureChest**.
- **Expected Result:** The plugin identifies the target chest as a treasure chest and cancels the creation.
- **Actual Result:** The plugin detected the chest is already a treasure chest and provided the ID of said treasure
  chest, failing to create the new one.
- **Status:** PASS.

## 2. Treasure Chest Management

### Test Case 2.1: Opening Treasure Chest Settings

- **Objective**: Ensure the treasure chest manager and settings menu open correctly.
- **Steps**:
    1. Create a treasure chest with ID **MyTreasureChest**.
    2. Run the command `/treasurehunt manager`.
    3. On the opened menu, click the **MyTreasureChest** button.
- **Expected Result:** Both menus open correctly and the treasure chest settings are displayed.
- **Actual Result:** Menus opened successfully and the settings button were displayed.
- **Status:** PASS.

### Test Case 2.2: Teleporting to Treasure Chests

- **Objective**: Verify the teleport utility function executes correctly.
- **Steps**:
    1. Create a treasure chest with ID **MyTreasureChest**.
    2. Open the treasure chest manager menu.
    3. While holding shift, left-click the **MyTreasureChest** button.
- **Expected Result:** The player gets teleported to the treasure chest without getting the clicked item in its
  inventory.
- **Actual Result:** The player was teleported and no additional items were added to its inventory.
- **Status:** PASS.

### Test Case 2.3: Adding Treasure Items

- **Objective**: Confirm that treasure items can be added to a treasure chest.
- **Steps**:
    1. Create a treasure chest with ID **MyTreasureChest**.
    2. Put some item(s) into the player inventory.
    3. Open the treasure chest manager menu.
    4. Click the **MyTreasureChest** button.
    5. Click the items from the player inventory.
- **Expected Result:** The clicked items get added to the treasure chest as rewards.
- **Actual Result:** The items got added as expected.
- **Status:** PASS.

### Test Case 2.4: Modifying Treasure Item Amount

- **Objective**: Confirm that treasure item amounts can be modified without exceeding vanilla stack sizes.
- **Steps**:
    1. Create a treasure chest with ID **MyTreasureChest**.
    2. Open the treasure chest manager menu.
    3. Click the **MyTreasureChest** button.
    4. Click on the reward items (not the ones from the bottom row).
        1. Left-click will increase quantity by 1.
        2. Right-click will decrease quantity by 1.
    5. Try to increase the amount of an item past/below its allowed stack size.
- **Expected Result:** Amounts change correctly without exceeding the allowed stack size.
- **Actual Result:** The amounts were successfully modified without exceeding vanilla amounts.
- **Status:** PASS.

### Test Case 2.5: Remove Treasure Items

- **Objective**: Ensure treasure items can be removed from treasure chests.
- **Steps**:
    1. Create a treasure chest with ID **MyTreasureChest**.
    2. Open the treasure chest manager menu.
    3. Click the **MyTreasureChest** button.
    4. Shift + right-click a reward item.
- **Expected Result:** The item gets deleted.
- **Actual Result:** The item was successfully deleted.
- **Status:** PASS.

### Test Case 2.6: Modify Treasure Item Spawn Chance

- **Objective**: Verify the spawn chance of treasure items can be modified without going below/above 0-100.
- **Steps**:
    1. Create a treasure chest with ID **MyTreasureChest**.
    2. Open the treasure chest manager menu.
    3. Click the **MyTreasureChest** button.
    4. Shift + left-click a reward item.
    5. Send '101' as the new spawn chance as a chat message.
    6. Once the error message is received, send '20' as the new spawn chance as a chat message.
- **Expected Result:** The spawn chance is modified and the treasure chest settings menu is opened again within the
  0-100 range.
- **Actual Result:** The chance was modified and the menu was opened within the 0-100 range.
- **Status:** PASS.

## 3. Treasure Hunt

### Test Case 3.1: Command Treasure Hunt Start

- **Objective**: Ensure the `/treasurehunt start <TreasureChest>` command works.
- **Steps**:
    1. Create a treasure chest with ID **MyTreasureChest**.
    2. Run the command `/treasurehunt start MyTreasureChest`
- **Expected Result:** A message is broadcast indicating the treasure hunt started and the treasure chest gets fill with
  loot.
- **Actual Result:** The message is displayed correctly and the chest is filled with loot.
- **Status:** PASS.

### Test Case 3.2: Manager Treasure Hunt Start

- **Objective**: Confirm that treasure hunts can be started through the treasure chest manager menu.
- **Steps**:
    1. Create a treasure chest with ID **MyTreasureChest**.
    2. Open the treasure chest manager menu.
    3. Click on the **MyTreasureChest** button.
    4. Click on the **Start Hunt** button.
- **Expected Result:** A message is broadcast indicating the treasure hunt started and the treasure chest gets fill with
  loot.
- **Actual Result:** The message is displayed correctly and the chest is filled with loot.
- **Status:** PASS.

### Test Case 3.3: Starting 2 Treasure Hunts at Once

- **Objective**: Verify that only one treasure hunt can be executed at once.
- **Steps**:
    1. Create a treasure chest with ID **MyTreasureChest**.
    2. Open the treasure chest manager menu.
    3. Click on the **MyTreasureChest** button.
    4. Click on the **Start Hunt** button.
    5. Click on the **Start Hunt** button a second time.
- **Expected Result:** A message is received indicating that a treasure hunt is already running.
- **Actual Result:** Message is received and the new treasure hunt doesn't start.
- **Status:** PASS.

### Test Case 3.4: Getting Treasure Hunt Clue

- **Objective**: Ensure the `/treasurehunt getclue` command works correctly.
- **Steps**:
    1. Create a treasure chest with ID **MyTreasureChest**.
    2. Start a treasure hunt for the treasure chest.
    3. Run the `/treasurehunt getclue` command.
- **Expected Result:** The player receives the map clue.
- **Actual Result:** The map is successfully received.
- **Status:** PASS.

### Test Case 3.5: Completing a Treasure Hunt

- **Objective**: Verify that treasure hunts end once a player opens the target treasure chest.
- **Steps**:
    1. Create a treasure chest with ID **MyTreasureChest**.
    2. Start a treasure hunt for the treasure chest.
    3. Go to the treasure chest's location.
    4. Right-click the treasure chest.
- **Expected Result:** The chest inventory opens, message is broadcast indicating the treasure hunt ended and the
  treasure hunt score of the player increases.
- **Actual Result:** Inventory opens, message is displayed and score is increased.
- **Status:** PASS.

### Test Case 3.6: Command Treasure Hunt Stop

- **Objective**: Confirm that treasure hunts can be stopped through the `/treasurehunt stop <TreasureChest>` command.
- **Steps**:
    1. Create a treasure chest with ID **MyTreasureChest**.
    2. Start a treasure hunt for the treasure chest.
    3. Run the `/treasurehunt stop <MyTreasureChest>` command.
- **Expected Result:** The treasure hunt stops, a message is broadcast indicating it was cancelled and the chest
  inventory is emptied.
- **Actual Result:** The hunt stops, the message is displayed and the inventory is cleared.
- **Status:** PASS.

### Test Case 3.7: Manager Treasure Hunt Stop

- **Objective**: Verify that treasure hunts can be stopped through the treasure chest manager.
- **Steps**:
    1. Create a treasure chest with ID **MyTreasureChest**.
    2. Start a treasure hunt for the treasure chest.
    3. Open the treasure chest manager menu.
    4. Click on the **MyTreasureChest** button.
    5. Click on the **Stop Hunt** button.
- **Expected Result:** The treasure hunt stops, a message is broadcast indicating it was cancelled and the chest
  inventory is emptied.
- **Actual Result:** The hunt stops, the message is displayed and the inventory is cleared.
- **Status:** PASS.

### Test Case 3.8: Not Existent Treasure Hunt Stop

- **Objective**: Ensure that stopping a treasure hunt that doesn't exist doesn't break the plugin's logic.
- **Steps**:
    1. Create a treasure chest with ID **MyTreasureChest**.
    2. Without starting a treasure hunt, open the treasure chest manager menu.
    3. Click on the **MyTreasureChest** button.
    4. Click on the **Stop Hunt** button.
- **Expected Result:** A message is received indicating that there is no treasure hunt for that chest running.
- **Actual Result:** The message is received and the plugin continues to work normally.
- **Status:** PASS.

### Test Case 3.9: Treasure Hunters Leaderboard

- **Objective**: Confirm the treasure hunters leaderboard opens correctly.
- **Steps**:
    1. Obtain hunter points in 1 or more accounts by completing treasure hunts.
    2. Run the `/treasurehunt leaderboard` command.
- **Expected Result:** The treasure hunter leaderboard menu is opened, listing the ranking of the top 10 players with
  the most hunt points.
- **Actual Result:** The menu is opened and the ranking is displayed correctly.
- **Status:** PASS.

### Test Case 3.10: Configurable Treasure Chest Permission

- **Objective**: Verify the configurable permission to open treasure chests is being checked.
- **Steps**:
    1. Create a treasure chest with ID **MyTreasureChest**.
    2. Start a treasure hunt for the treasure chest.
    3. Go to the treasure chest location.
    4. As a player without the configured permission, try to open the chest.
- **Expected Result:** The player isn't allowed to open the chest and receives a message indicating so.
- **Actual Result:** The chest opening is cancelled and the message is received.
- **Status:** PASS.

## 4. Treasure Chest Removal

### Test Case 4.1: Treasure Chest Remove Command

- **Objective**: Confirm the command for removing chests works correctly.
- **Steps**:
    1. Create a treasure chest with ID **MyTreasureChest**.
    2. Run the command `/treasurehunt removechest MyTreasureChest`.
- **Expected Result:** The treasure chest gets removed and its database entries are cleared.
- **Actual Result:** The treasure chest got successfully deleted both from memory and the database.
- **Status:** PASS.

### Test Case 4.2: Treasure Chest Manager Remove

- **Objective**: Verify the treasure manager menu allows for treasure chest removal.
- **Steps**:
    1. Create a treasure chest with ID **MyTreasureChest**.
    2. Open the treasure chest manager.
    3. Click on the treasure chest with ID **MyTreasureChest**.
    4. Click on the **Delete** barrier block button.
    5. Click on the button again to confirm the deletion.
- **Expected Result:** After being asked for confirmation, the treasure chest gets removed and its database entries are
  cleared.
- **Actual Result:**  The treasure chest is deleted both from memory and the database.
- **Status:** PASS.

### Test Case 4.3: Treasure Chest Breaking

- **Objective**: Ensure treasure chests can't be broken by hand and crash the plugin's functionality.
- **Steps**:
    1. Create a treasure chest with ID **MyTreasureChest**.
    2. Break the chest block.
- **Expected Result:** The break event is cancelled and a message is sent to the player.
- **Actual Result:** The break event was cancelled and a message was received with instructions to remove the treasure
  chest.
- **Status:** PASS.

### Test Case 4.4: Treasure Chest Explosion

- **Objective**: Check that treasure chests can't be exploded either by blocks or entities.
- **Steps**:
    1. Create a treasure chest with ID **MyTreasureChest**.
    2. Cause an explosion next to it.
        1. To test entity explosions, place a TNT and ignite it with flint and steel.
        2. To test block explosions, place a respawn anchor and right-click it 5 times with glowstone.
- **Expected Result:** The explosion occurs and all usual blocks are broken, except the treasure chest.
- **Actual Result:** The explosions occurred and the treasure chest remained in place.
- **Status:** PASS.