---@alias CraftItemResult { missItem: boolean, missItems: { }, cancel: function(): nil }

--- Utility function from auto crafting
---@param device table AE Interface
---@param id string Item or fluid id
---@param amount number Item amount or Fluid amount(Mb)
---@return CraftItemResult Crafting result
local function craftItem(device, id, amount)
    local manager = device.getCraftingManager()
    local craftingRequest = manager.createCraftingRequest(id, amount)

    if craftingRequest == nil then
        error("Cannot create crafting request from item '" .. id .. "'", 2)
    end

    while not craftingRequest.isReady() do
        sleep(0.05)
    end

    if craftingRequest.isMissingItems() then
        return {
            missItem = true,
            missItems = craftingRequest.getMissingItems(),
			id = nil,
            cancel = function()
            end
        }
    else
        local result = craftingRequest.start()

		while not result.isSuccess() do
			sleep(0.05)
		end

        return {
            missItem = false,
            missItems = {},
			id = result.getId(),
            cancel = function()
				result.cancel()
            end
        }
    end
end

_ENV.craftItem = craftItem
