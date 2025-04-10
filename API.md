# 设备接口

| Method                    | Description                    |
|---------------------------|--------------------------------|
| `getCraftingManager()`    | 获取自动合成管理器                      |
| `getEnergyManager()`      | 获取能源管理器                        |
| `getOnlineMachineCount()` | 获取连接到网络的机器数量，不包括离线或缺失可用频道频道的机器 |
| `getStorageManager()`     | 获取存储管理器                        |
| `getUsedChannelCount()`   | 获取网络已使用的频道数量                   |
| `getMachineCount()`       | 获取连接到网络的机器数量                   |
| `isActive()`              | 系统是否在线                         |
| `isMissingChannel()`      | 当前设备是否缺失可用频道                   |
| `isPowered()`             | 当前设备是否已启动                      |

## 能源

### EnergyManager

| Method                   | Description            |
|--------------------------|------------------------|
| `getIdlePowerUsage()`    | 获取系统空闲时每tick的能源使用量(FE) |
| `getChannelPowerUsage()` | 获取频道空闲能量使用情况(FE)       |

