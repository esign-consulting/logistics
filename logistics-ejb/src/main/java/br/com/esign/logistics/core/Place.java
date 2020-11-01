/* 
 * The MIT License
 *
 * Copyright 2015 Esign Consulting Ltda.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.com.esign.logistics.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Transient;

/**
 * 
 * @author gustavomunizdocarmo
 */
@Embedded
public class Place implements Comparable<Place> {
	
    private final String name;
    
    @Transient @JsonIgnore
    private final Set<Place> destinations = new HashSet<>();
    
    private Place() {
        this.name = null;
    }
    
    public Place(String name) {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException();
        
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public Set<Place> getDestinations() {
        return destinations;
    }

    public void addDestination(Place destination) {
        if (destination == null || destination.equals(this))
            throw new IllegalArgumentException();
        
        destinations.add(destination);
    }
    
    @Override
    public int compareTo(Place o) {
        return name.compareTo(o.getName());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Place other = (Place) obj;
        return Objects.equals(this.name, other.name);
    }

    @Override
    public String toString() {
        return name;
    }
	
}