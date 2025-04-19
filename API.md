<style>
yellow-font {
	color: yellow;
}
</style>

# API

## Table of contents

- [Interfaces](#interfaces)
- [Utils](#utils)
- [Objects](#objects)

# Interfaces

<yellow-font> Unless otherwise specified, most APIs will return -1 or nil when the network is inaccessible (power off/missing available channels). </yellow-font>

| Method                    | Result                                 | Description                                                                                                         |
|---------------------------|----------------------------------------|---------------------------------------------------------------------------------------------------------------------|
| `getCraftingManager()`    | [`CraftingManager`](#crafting-manager) | Get the automatic crafting manager                                                                                  |
| `getEnergyManager()`      | [`EnergyManager`](#energy-manager)     | Get the energy manager                                                                                              |
| `getOnlineMachineCount()` | `number`                               | Get the number of machines connected to the network, excluding offline machines or those missing available channels |
| `getStorageManager()`     | [`StorageManager`](#storage-manager)   | Get the storage manager                                                                                             |
| `getUsedChannelCount()`   | `number`                               | Get the number of channels used by the network                                                                      |
| `getMachineCount()`       | `number`                               | Get the number of machines connected to the network                                                                 |
| `isActive()`              | `bool`                                 | Is the system online                                                                                                |
| `isMissingChannel()`      | `bool`                                 | Is the current device missing available channels                                                                    |
| `isPowered()`             | `bool`                                 | Is the current device powered on                                                                                    |
| `reset()`                 | `nil`                                  | Reset interface state                                                                                               |

### Energy Manager

| Method                   | Result   | Description                                            |
|--------------------------|----------|--------------------------------------------------------|
| `getAvgPowerInput()`     | `number` | Get average energy input (FE)                          |
| `getAvgPowerUsage()`     | `number` | Get average energy usage (FE)                          |
| `getChannelPowerUsage()` | `number` | Get channel idle energy usage (FE)                     |
| `getIdlePowerUsage()`    | `number` | Get energy usage per tick when the system is idle (FE) |
| `getMaxStoredPower()`    | `number` | Get the maximum energy the network can store (FE)      |
| `getStoredPower()`       | `number` | Get the energy stored in the network (FE)              |

### Storage Manager

| Method                         | Result                                                    | Description                                                                                                                                                             |
|--------------------------------|-----------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `getDetails()`                 | [`{[string]: { ItemDetail } }`](#item-detail-itemdetail)  | Get the table of item data stored in the network<br><font-yellow>Note: The second layer of the list handles items with the same id but different Nbt data</font-yellow> |
| `getFluidAmountMb(id: string)` | `number`                                                  | Return the amount of fluid with the specified id stored in the network (Mb)                                                                                             |
| `getItemCount(id: string)`     | `number`                                                  | Return the number of items with the specified id stored in the network                                                                                                  |
| `getTypeCount()`               | [`ItemTypeCount`](#storage-item-type-count-itemtypecount) | Get the number of types of items/fluids stored in the network                                                                                                           |

### Crafting Manager

| Method                                              | Result                                                          | Description                                                        |
|-----------------------------------------------------|-----------------------------------------------------------------|--------------------------------------------------------------------|
| `createCraftingRequest(id: string, amount: number)` | [`CraftingRequest`](#auto-crafting-request-autocraftingrequest) | Create an automatic crafting request based on item id and quantity |
| `getCpus()`                                         | [`{ CpuInfo }`](#cpu-info-cpuinfo)                              | Get information about all CPUs in the network                      |
| `getCpuCount()`                                     | `number`                                                        | Get the number of CPUs connected to the network                    |
| `getIdleCPUCount()`                                 | `number`                                                        | Get the number of idle CPUs in the network                         |

# Utils

## AE Crafting Util (aecraftimgutil)

| Method                                       | Result  | Description                                                          |
|----------------------------------------------|---------|----------------------------------------------------------------------|
| `craftItem(device, id: string, amount: int)` | `table` | Craft the specified number of items or fluids via automatic crafting |

## AE Util (aeUtil)

| Method                   | Result   | Description      |
|--------------------------|----------|------------------|
| `AEtoFE(energy: number)` | `number` | Convert AE to FE |
| `FEtoAE(energy: number)` | `number` | Convert FE to AE |

# Objects

## Crafting util result (CraftingResult)

<details> 
    <summary>Field</summary>

| Key            | Type             | Description                                                       |
|----------------|------------------|-------------------------------------------------------------------|
| `id`           | `string`         | The id of the crafting task                                       |
| `missingItem`  | `bool`           | Whether missing materials were found during crafting              |
| `missingItems` | `{ ItemDetail }` | Lists all missing items and quantities if `missingItem` is `true` |

</details>

<details> 
    <summary>Function</summary>

| Function   | Result | Description              |
|------------|--------|--------------------------|
| `cancel()` | `nil`  | Cancel the crafting task |

</details>

## Item detail (ItemDetail)

<details> 
    <summary>Field</summary>

| Key | Type | Description |
|-----|------|-------------|

</details>

<details> 
    <summary>Function</summary>

| Function     | Result   | Description            |
|--------------|----------|------------------------|
| `getCount()` | `number` | Get the count          |
| `getId()`    | `string` | Get the ID             |
| `isFluid()`  | `bool`   | Is it a fluid?         |
| `isItem()`   | `bool`   | Is it an item?         |
| `getTag()`   | `table`  | Get the item's Nbt tag |

</details>

## Storage item type count (ItemTypeCount)

<details> 
    <summary>Field</summary>

| Key     | Type     | Description           |
|---------|----------|-----------------------|
| `item`  | `number` | Number of item types  |
| `fluid` | `number` | Number of fluid types |

</details>

<details> 
    <summary>Function</summary>

| Function | Result | Description |
|----------|--------|-------------|

</details>

## Auto crafting request (AutoCraftingRequest)

<details> 
    <summary>Field</summary>

| Key | Type | Description |
|-----|------|-------------|

</details>

<details> 
    <summary>Function</summary>

| Function            | Result                                                           | Description                                                                                                                          |
|---------------------|------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------|
| `isReady()`         | `bool`                                                           | Check if the crafting computation is complete                                                                                        |
| `isMissingItems()`  | `bool`                                                           | Check if materials are missing (including methods that cannot be crafted), will always return `false` before computation is complete |
| `getMissingItems()` | [`{ ItemDetail }`](#item-detail-itemdetail)                      | Get the list of missing items, will always return an empty list before computation is complete                                       |
| `start()`           | [`AutoCraftingResult`](#auto-crafting-result-autocraftingresult) | Start the automatic crafting, calling this before computation is complete will cause an error                                        |

</details>

## Auto crafting result (AutoCraftingResult)

<details> 
    <summary>Field</summary>

| Key | Type | Description |
|-----|------|-------------|

</details>

<details> 
    <summary>Function</summary>

| Function       | Result    | Description                                                       |
|----------------|-----------|-------------------------------------------------------------------|
| `isSuccess()`  | `bool`    | Whether the automatic crafting request was successfully submitted |
| `getId()`      | `string?` | Get the id of the crafting task                                   |
| `cancel()`     | `nil`     | Cancel the crafting task                                          |
| `isCanceled()` | `bool`    | Check if the crafting task was canceled                           |

</details>

## Cpu info (CpuInfo)

<details> 
    <summary>Field</summary>

| Key | Type | Description |
|-----|------|-------------|

</details>

<details> 
    <summary>Function</summary>

| Function            | Result                                    | Description                                                                                                 |
|---------------------|-------------------------------------------|-------------------------------------------------------------------------------------------------------------|
| `isBusy()`          | `bool`                                    | Is the CPU currently working on crafting                                                                    |
| `cancelJob()`       | `nil`                                     | Cancel the current crafting task the CPU is working on                                                      |
| `getCoProcessors()` | `number`                                  | Get the number of co-processors installed on the CPU                                                        |
| `getJobStatus()`    | [`JobStatus?`](#cpu-job-status-jobstatus) | Get the information of the current crafting task the CPU is processing, returns `nil` if no task is ongoing |
| `getName()`         | `string?`                                 | Get the CPU's name, returns `nil` if no name is set                                                         |

</details>

## Cpu job status (JobStatus)

<details> 
    <summary>Field</summary>

| Key | Type | Description |
|-----|------|-------------|

</details>

<details> 
    <summary>Function</summary>

| Function                | Result                                  | Description                                 |
|-------------------------|-----------------------------------------|---------------------------------------------|
| `getCraftingItem()`     | [`ItemDetail`](#item-detail-itemdetail) | Get the crafting target                     |
| `getTotalItems()`       | `number`                                | Get the total number of items being crafted |
| `getProgress()`         | `number`                                | Get the number of items already crafted     |
| `getElapsedTimeNanos()` | `number`                                | Get the estimated completion time (Ns)      |

</details>
