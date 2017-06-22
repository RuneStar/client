package com.runesuite.client.base.access;

import com.runesuite.client.base.Accessor;
import java.lang.String;

public interface XWorld extends Accessor {
    @Field
    String getActivity();

    @Field
    void setActivity(String value);

    @Field
    String getHost();

    @Field
    void setHost(String value);

    @Field
    int getId();

    @Field
    void setId(int value);

    @Field
    int getIndex();

    @Field
    void setIndex(int value);

    @Field
    int getLocation();

    @Field
    void setLocation(int value);

    @Field
    int getPopulation();

    @Field
    void setPopulation(int value);

    @Field
    int getProperties();

    @Field
    void setProperties(int value);
}
