
#include "JavaProperty.h"

JavaProperty::JavaProperty()
{
}

JavaProperty::JavaProperty(const string& name, const string& value)
{
     m_name = name;
     m_value = value;
}

const string& JavaProperty::getName() const
{
   return m_name;
}
    
const string& JavaProperty::getValue() const
{
    return m_value;
}

