Dumps or inspects any objects and converts to readable format string. This library consists of one file only.

-sample
```
   import static tetz42.util.ObjDumper4j.*;
         :
     Logger.debug( dumper("The result object:\n", theResult) );
     Logger.debug( inspecter("foo") );
```

# Dump #
There are two dump method, 'dump' and 'dumper'.

'dump' method converts parameters to readable format String.

'dumper' method creates an object to dump the parameters.
When 'toString' method of the object is called, the parameters will convert to readable format String.

Both dump parameters as follows:

- Array, List, Set
```
  String[]@13b8f864[
    "foo"
    "bar"
    "baz"
  ]
```

- Map
```
  HashMap@54bb7759{
    "key1": "value1"
    "key2": "value2"
  }
```

- Bean
```
   Bean@7cf1bb78{
     intField = 10
     floatField = 10.0
     strField = "10"
   }
```

- mix
```
   HashMap@54bb7759{
     "key1": Bean@7cf1bb78{
       intField = 10
       floatField = 10.0
       mapField = HashMap@7f2a3793{
         "key1": 100
         "key2": 10000
       }
     }
     "key2": ArrayList@50d5db23[
       "foo"
       "bar"
       "baz"
     ]
   }
```


You can use 'dumper' method as follows:
```
  Logger.debug( dumper("Contents of DTO = ", dto) );
```
The result:
```
   [DEBUG] Contents of DTO = FooDTO@13b8f864{
     id = 42
     name = "Tetz"
     sex = "male"
     jobs = ArrayList@37bd2664[
       "IT engineer"
       "Farmer"
       "Husband"
       "Farther"
     ]
   }
```


The object created by 'dumper' method do nothing unless its 'toString' method is performed.
Therefore, if you use wise logging product like log4j, you need not care about performance problem when you write the code like below:
```
   log.debug( dumper("Result:\n", theResult) );
```
instead of:
```
   if( log.isDebugEnabled() ) {
     log.debug( dumper("Result:\n", theResult) );
   }
```
Because log4j does not perform 'toString' method unless the log is output.


# Inspect #

There are two inspect method, 'inspect' and 'inspecter'.

Both convert parameters as follows:

-sample
```
  System.out.println( inspecter("foo") );
```
The result:
```
   String@18cc6{
     value = char[]@7a187814[
       f
       o
       o
     ]
     offset = 0
     count = 3
     hash = 101574
     serialVersionUID = -6849794470754667710
     serialPersistentFields = ObjectStreamField[]@314c194d[ ]
     CASE_INSENSITIVE_ORDER = CaseInsensitiveComparator@23394894{
       serialVersionUID = 8575799808933029326
     }
   }
```

# Algorithm #

You can choose five algorithms against reference cycles.

- superRapid
```
     // Very fast. Error will occur if the parameter is a bean that has a reference to itself.
     Logger.debug( dumper(dto).superRapid() );
```

- rapid
```
     // Fast. Depends on the parameter's 'hashCode' or 'equals' method, it might not act properly.
     Logger.debug( dumper(dto).rapid() );
```

- normal
```
     // Normal. Depends on the parameter's 'hashCode' method, it might not act properly.
     Logger.debug( dumper(dto).normal() );
```

- safe
```
     // Safe.
     Logger.debug( dumper(dto) );
```

- superSafe
```
     // Very safe. Error won't occur even if the parameter is a List, Set or Map that has a reference to itself.
     Logger.debug( dumper(list).superSafe() );
```