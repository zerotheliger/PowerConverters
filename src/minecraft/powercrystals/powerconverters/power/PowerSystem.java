package powercrystals.powerconverters.power;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraftforge.common.Configuration;
import powercrystals.powerconverters.PowerConverterCore;

public class PowerSystem
{
    private static Map<Integer, PowerSystem> _powerSystems = new HashMap<Integer, PowerSystem>();
    private static Integer _nextPowerSystemId = 0;

    private String _abbreviation;
    private String _name;
    private float _internalEnergyPerInput;
    private float _internalEnergyPerOutput;
    private String[] _voltageNames;
    private int[] _voltageValues;
    private String _unit;
    private int _id;

    public PowerSystem(String name, String abbreviation, float energyPerInput, float energyPerOutput, String[] voltageNames, int[] voltageValues, String unit)
    {
	_name = name;
	_abbreviation = abbreviation;
	_internalEnergyPerInput = energyPerInput;
	_internalEnergyPerOutput = energyPerOutput;
	_voltageNames = voltageNames;
	_voltageValues = voltageValues;
	_unit = unit;
    }

    public static void registerPowerSystem(PowerSystem powerSystem)
    {
	_powerSystems.put(_nextPowerSystemId, powerSystem);
	powerSystem._id = _nextPowerSystemId;
	_nextPowerSystemId++;
    }

    public static PowerSystem getPowerSystemById(int id)
    {
	return _powerSystems.get(id);
    }

    public String getAbbreviation()
    {
	return _abbreviation;
    }

    public static void loadConfig(Configuration c)
    {
	String powerRatioComment = "Not all power systems listed here are necessarily used; they may be provided so that\r\n" + "the ratios are all stored in a single place and for possible future use.";

	c.addCustomCategoryComment("PowerRatios", powerRatioComment);

	for (Entry<Integer, PowerSystem> p : _powerSystems.entrySet())
	{
	    String configSection = "PowerRatios." + p.getValue()._name;
	    p.getValue()._internalEnergyPerInput = (float) c.get(configSection, p.getValue()._name + "InternalEnergyPerEachInput", p.getValue()._internalEnergyPerInput).getDouble(p.getValue()._internalEnergyPerInput);
	    p.getValue()._internalEnergyPerOutput = (float) c.get(configSection, p.getValue()._name + "InternalEnergyPerEachOutput", p.getValue()._internalEnergyPerOutput).getDouble(p.getValue()._internalEnergyPerOutput);
	}
	PowerConverterCore.powerSystemSteamEnabled = c.get("PowerRatios.Steam", "Enable Steam Converters", true).getBoolean(true);
    }

    public float getInternalEnergyPerInput()
    {
	return _internalEnergyPerInput;
    }

    public float getInternalEnergyPerOutput()
    {
	return _internalEnergyPerOutput;
    }

    public String[] getVoltageNames()
    {
	return _voltageNames;
    }

    public int[] getVoltageValues()
    {
	return _voltageValues;
    }

    public String getUnit()
    {
	return _unit;
    }

    public int getId()
    {
	return _id;
    }
}
