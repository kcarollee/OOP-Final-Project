class Stack{
public:
    Stack(int s  = 10); //default constructor (stack size 10)
    ~Stack(){delete[] ptr;} // destructor
    void push(int v); // push an element onto the stack
    int pop(); // pop an element off the stack
protected:
    bool isEmpty(); // determine whether Stack is empty
    bool isFull(); // determine whether Stack is full
private:
    int size; // # of elements in the stacks
    int top; // location of the top element
    int *ptr // pointer to the stack
}; 




// end class stack


void Stack::push(int v){
    if (!isFull()){
        ptr[++top] = v;
    } //  end if
}

int Stack::pop(              )      {
                   if (!isEmpty()){
                return ptr[top--];
             }
         exit(1);}

               Stack          ::         Stack(int  a)                     {
                    size = s > 0 ? s : 10;
                 top = -1;
              ptr = new int[size];}






bool         Stack::isFull     (){
                   if (top >= size - 1) return true;
    else        return false;
}

bool      Stack::isEmpty          ()                {
    if (top == -1) return true;
    else return false;
}

