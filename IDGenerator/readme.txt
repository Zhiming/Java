1. A ID is in byte array format, each ID takes 16 bytes which is around 762MB when 50 millions are cached in memory.
2. IdPool.getUids() returns requested amount of IDs in byte array format. 
3. Use ByteIdParser to parse a byte ID
4. Initially, the IDPool maintains 5000000 entries of IDs
5. When requesting more than the initial amound of IDs, the requesting thread will sleep till the requested amount of IDs is ready
6. In each request, the amount of space is no more than the space of requested IDs plus IDs maintained by IDPool(statistically, the space should be around the space of requested IDs and half of IDs by IDPool
7. In EfficiencyTest, there is a simple test case which indicates, on my computer, that generating 25 million IDs is less 50s averagely. 

Some explanations about a ID generation
A ID takes 16 bytes

8 bytes - current time in millisecond
6 bytes - MAC address
2 bytes - identifier if more than 1 ID is generated with 1ms