/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.xream.x7.repository.redis.lock;

import io.xream.x7.lock.DistributionLock;
import io.xream.x7.lock.LockProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

@Component
public class DefaultLockProvider implements LockProvider {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate = stringRedisTemplate;
        DistributionLock.init(this);
    }

    @Override
    public boolean lock(String key, @NotNull Integer timeOut){
        if (timeOut.intValue() == 0)
            timeOut = DEFAULT_TIMEOUT;
        return this.stringRedisTemplate.opsForValue().setIfAbsent(key, VALUE,timeOut,TimeUnit.MILLISECONDS);
    }

    @Override
    public void unLock(DistributionLock.Lock lock){
        this.stringRedisTemplate.delete(lock.getKey());
    }
}
