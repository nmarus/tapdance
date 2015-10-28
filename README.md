#tapdance#
Cisco TAPS CLI interface to associate BAT DN with an Auto Registered DN. Tested with JRE 1.7 and CUCM 10.5.X

**Arguments:**

*Directory number of Auto Registered phone:*

  > -a, --auto-dn <arg>

*Directory number of BAT phone:*

  > -b, --bat-dn <arg>

*Cisco Communication Manager RMI Hostname or IP:*

  > -r, --rmi-host <arg>

*Help:*

  > -h, --help


**Return Codes:**

 * 8   Incomplete
 * 7   Not Found
 * 6   Not an Auto Registered device
 * 5   Not a BAT Device
 * 3   Duplicate DN (See Example 2 for using EPNM)
 * 2   Incomplete
 * 1   Protected DN
 * 0   Successfull

**Examples:**

*Standard Registration*

  > java -jar tapdance.jar -r 192.168.10.40 -b 65022 -a 60001
  
*Duplicate DN with added EPNM*
  
  > java -jar tapdance.jar -r 192.168.10.40 -b 65022::6785554001 -a 60002
  
*E.164 Support*
  
  > java -jar tapdance.jar -r 192.168.10.40 -b \\+16785554001 -a 60003
  
