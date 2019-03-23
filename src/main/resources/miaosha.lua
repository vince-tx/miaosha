local count = redis.call('get', KEYS[1])
if count then redis.call('decr', num)
    return 1
else return 0
end
