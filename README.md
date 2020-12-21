# rPets
A MiniaturePets addon made for Retrox.eu<br>

## Dependecies
- PlaceholderAPI
- MiniaturePets

#Rules
All petnames are lowercased, alpha-numeric strings.<br>
All config keys (blocknames, entity names) should be lowercased.


## Placeholders
In case a player is not specified or player doesnt exists null is returned.<br>
In case pet is not specified, or pet doesnt exists null is returned.<br>
In case format not specified, lvl is returned<br>
Format can be: `lvl, exp` <br> 
<br>
These are `<required>` and `(optional)` parameters<br>

### Required experience
Parameters: `<petname>` <br>
Petname can be swapped with "current" to represent current pet of the online player<br>
Example:
```
rpets_required_boxer
rpets_required_current
```

### Current experience
Parameters: `<petname>, (format)` <br>
Petname can be swapped with "current" to represent current pet of the online player<br>
Example:
```
rpets_experience_boxer_exp // returns boxer's exp
rpets_required_boxer // returns boxer's lvl
rpets_required_boxer_lvl // retuns boxer's lvl also
```
