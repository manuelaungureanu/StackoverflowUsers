# StackoverflowUsers

Brief notes on what I used and why:

1. MVP for testability reasons and good separation of concerns.
2. Native Data Binding library to remove boiler plate code
3. Loaders since they are lifecycle aware and solve any issue that might happen at screen rotation.
4. Injection instead of a dependency injection framework for lack of time.
5. Glide library to download and display the profile image for convenience reasons: low time taken to load the image, caching mecanism.
6. Scanner to download data - for buffering and UTF-16 encoding provided.
