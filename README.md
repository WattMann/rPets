# rPets
A MiniaturePets addon made for Retrox.eu<br>

## Dependecies
- PlaceholderAPI
- MiniaturePets


## Placeholders
All petnames are lowercased, alpha-numeric strings.<br>
In case a player is not specified or player doesnt exists null is returned.<br>
In case pet is not specified, or pet doesnt exists null is returned.<br>
In case xp or lvl is not specified, lvl is returned<br>

#### rpets_xp
This placeholder represents the xp value of a pet <br>
params: `[petname:string]` <br>
e.g: ``` %rpets_xp_boxer% => 2096```<br>


#### rpets_lvl
This placeholder represents the lvl of a pet <br>
params: `[petname:string]`<br>
e.g: ``` %rpets_lvl_minime% => 15```<br>

#### rpets_requiredxp
This placeholder represents the required xp to gain a specified level<br>
params: `[level:integer]`<br>
e.g ```%rpets_requiredxp_7% => 4064```<br>
